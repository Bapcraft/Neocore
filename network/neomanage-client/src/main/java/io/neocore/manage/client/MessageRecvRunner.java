package io.neocore.manage.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;
import java.util.logging.Level;

import io.neocore.api.NeocoreAPI;
import io.neocore.manage.proto.NeomanageProtocol.ClientMessage;

public class MessageRecvRunner implements Runnable {
	
	private volatile boolean running = true;
	
	private String daemonName;
	
	private InputStream stream;
	private Consumer<ClientMessage> recvCallback;
	
	public MessageRecvRunner(String name, InputStream stream, Consumer<ClientMessage> callback) {
		
		this.daemonName = name;
		
		this.stream = stream;
		this.recvCallback = callback;
		
	}
	
	public void stop() {
		this.running = false;
	}
	
	@Override
	public void run() {
		
		while (this.running) {
			
			try {
				
				ClientMessage msg = ClientMessage.parseDelimitedFrom(this.stream);
				NeocoreAPI.getLogger().finer("Got message ID:" + Long.toHexString(msg.getMessageId()) + " from " + this.daemonName + " of type " + msg.getPayloadCase().name() + ".");
				this.recvCallback.accept(msg);
				
			} catch (IOException e) {
				
				NeocoreAPI.getLogger().log(Level.SEVERE, "Problem parsing message from " + this.daemonName + "!", e);
				this.stop();
				
			}
			
		}
		
	}

}
