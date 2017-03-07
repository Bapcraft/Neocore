package io.neocore.manage.client.net;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.logging.Level;

import io.neocore.api.NeocoreAPI;
import io.neocore.manage.proto.ClientMessageUtils;
import io.neocore.manage.proto.NeomanageProtocol.ClientMessage;
import io.neocore.manage.proto.NeomanageProtocol.UnregisterClient;
import io.neocore.manage.proto.NeomanageProtocol.UnregisterClient.UnregisterReason;

public class NmServer implements Closeable {

	private Socket socket;
	private long timeout;

	private BlockingDeque<ClientMessage> queue = new LinkedBlockingDeque<>();

	private HandlerManager handlers;

	private MessageQueueRunner queueRunner;
	private Thread queueThread;
	private MessageRecvRunner recvRunner;
	private Thread recvThread;

	private long lastMessageTime = System.currentTimeMillis();

	public NmServer(Socket socket, long timeout, HandlerManager man) throws IOException {

		this.socket = socket;
		this.timeout = timeout;

		this.handlers = man;

		this.queueRunner = new MessageQueueRunner(this.queue, this.socket.getOutputStream());
		this.recvRunner = new MessageRecvRunner(this.getLabel(), this.socket.getInputStream(),
				m -> this.recvMessage(m));

		this.queueThread = new Thread(this.queueRunner, "NmQueueThread-" + this.getLabel());
		this.recvThread = new Thread(this.recvRunner, "NmRecvThread-" + this.getLabel());

		this.queueThread.start();
		this.recvThread.start();

	}

	public void queueMessage(ClientMessage message) {

		if (this.lastMessageTime + this.timeout < System.currentTimeMillis()) {

			long dur = System.currentTimeMillis() - this.lastMessageTime;
			NeocoreAPI.getLogger()
					.severe("Tried to queue a message to " + this.getLabel()
							+ ", but we haven't recieved a message from them in " + dur + " ms, vs timeout of "
							+ this.timeout + "!");
			this.close();

		} else {

			this.queue.add(message);
			NeocoreAPI.getLogger().finer("Queued message ID:" + Long.toHexString(message.getMessageId()) + " to server "
					+ this.getLabel() + ".");

		}

	}

	private void recvMessage(ClientMessage message) {

		this.update();
		this.handlers.handleMessage(this, message);

	}

	public void update() {
		this.lastMessageTime = System.currentTimeMillis();
	}

	@Override
	public synchronized void close() {

		if (this.socket.isClosed())
			return;

		try {

			NeocoreAPI.getLogger().info("Closing Neomanage connection to " + this.getLabel() + "...");

			// Stop workers...
			this.queueRunner.stop();
			this.queueThread.interrupt();
			this.recvRunner.stop();
			this.recvThread.interrupt();

			// Disconnect message.
			ClientMessage.Builder b = ClientMessageUtils.newBuilder(NeocoreAPI.getAgent().getAgentId());
			b.setUnregClient(UnregisterClient.newBuilder().setReason(UnregisterReason.SHUTDOWN)
					.setReasonStr("thx bb <3").build());

			// Actually send it.
			OutputStream os = this.socket.getOutputStream();
			b.build().writeDelimitedTo(os);
			os.flush();

			// Actually close it.
			this.socket.close();

		} catch (IOException e) {
			NeocoreAPI.getLogger().log(Level.SEVERE, "Problem when closing socket.", e);
		}

	}

	public synchronized void forceClose() {

		// Stop workers.
		this.queueRunner.stop();
		this.queueThread.interrupt();
		this.recvRunner.stop();
		this.recvThread.interrupt();

		try {

			// Just close it.
			this.socket.close();

		} catch (IOException e) {
			// ehh fuck it
		}

	}

	public boolean isOnline() {
		return !this.socket.isClosed();
	}

	public String getLabel() {
		return "nmd@" + this.socket.getInetAddress().getHostAddress() + ":" + this.socket.getPort();
	}

}
