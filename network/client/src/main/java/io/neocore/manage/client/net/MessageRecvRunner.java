package io.neocore.manage.client.net;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
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
				if (msg == null) continue; // wtf?
				
				NeocoreAPI.getLogger().finer("Got message ID:" + Long.toHexString(msg.getMessageId()) + " from " + this.daemonName + " of type " + msg.getPayloadCase().name() + ".");
				
				if (!msg.hasSenderId() || !UUID.fromString(msg.getSenderId()).equals(NeocoreAPI.getAgent().getAgentId())) {
					
					try {
						this.recvCallback.accept(msg);
					} catch (Throwable t) {
						NeocoreAPI.getLogger().log(Level.SEVERE, "Severe unknown error when processing Neomanage network packet!", t);
					}
					
				} else {
					NeocoreAPI.getLogger().warning("Got message from self of type " + msg.getPayloadCase() + ", ignoring.");
				}
				
			} catch (IOException e) {
				
				if (this.running) {
					
					NeocoreAPI.getLogger().log(Level.SEVERE, "Problem parsing message from " + this.daemonName + "!", e);
					this.stop();
					
				}
				
			}
			
		}
		
	}

}
