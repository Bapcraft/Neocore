package io.neocore.manage.client.net;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.BlockingDeque;
import java.util.function.Consumer;
import java.util.logging.Level;

import io.neocore.api.NeocoreAPI;
import io.neocore.manage.proto.NeomanageProtocol.ClientMessage;

public class MessageQueueRunner implements Runnable {

	private volatile boolean running = true;
	
	private BlockingDeque<ClientMessage> queue;
	private OutputStream stream;
	
	private Consumer<BlockingDeque<ClientMessage>> deathCallback;
	
	public MessageQueueRunner(BlockingDeque<ClientMessage> q, OutputStream out) {
		
		this.queue = q;
		this.stream = out;
		
	}
	
	public void stop() {
		this.running = false;
	}
	
	public void setDeathCallback(Consumer<BlockingDeque<ClientMessage>> callback) {
		this.deathCallback = callback;
	}
	
	@Override
	public void run() {
		
		while (this.running) {
			
			ClientMessage msg = null;
			
			try {
				
				msg = this.queue.take();
				
				msg.writeDelimitedTo(this.stream);
				this.stream.flush();
				
			} catch (IOException e) {
				
				NeocoreAPI.getLogger().log(Level.SEVERE, "Problem occured when flushing message!", e);
				if (msg != null) this.queue.addFirst(msg);
				
				this.stop();
				if (this.deathCallback != null) this.deathCallback.accept(this.queue);
				
			} catch (InterruptedException e) {
				NeocoreAPI.getLogger().warning("Interrupted while dequeueing send message.");
			}
			
		}
		
	}

}
