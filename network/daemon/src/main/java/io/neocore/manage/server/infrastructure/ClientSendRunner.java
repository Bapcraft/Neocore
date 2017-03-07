package io.neocore.manage.server.infrastructure;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.BlockingDeque;
import java.util.logging.Level;

import io.neocore.manage.proto.NeomanageProtocol.ClientMessage;
import io.neocore.manage.server.Nmd;
import io.neocore.manage.server.handling.HandlerRunner;

public class ClientSendRunner extends HandlerRunner {

	private BlockingDeque<ClientMessage> queue;
	private OutputStream stream;

	public ClientSendRunner(BlockingDeque<ClientMessage> q, OutputStream os) {

		this.queue = q;
		this.stream = os;

	}

	@Override
	public void cycle() {

		ClientMessage msg = null;

		try {

			msg = this.queue.take();

			if (msg != null) {

				msg.writeDelimitedTo(this.stream);
				this.stream.flush();

			}

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {

			if (this.isActive()) {

				Nmd.logger.log(Level.SEVERE, "Problem sending message!", e);
				if (msg != null)
					this.queue.addFirst(msg);

				this.disable();

			}

		}

	}

}
