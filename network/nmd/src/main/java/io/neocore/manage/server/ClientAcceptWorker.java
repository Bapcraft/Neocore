package io.neocore.manage.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;

public class ClientAcceptWorker implements Runnable {
	
	private volatile boolean accepting;
	
	private ServerSocket socket;
	private Consumer<Socket> connectionCallback;
	
	public ClientAcceptWorker(ServerSocket sock, Consumer<Socket> callback) {
		
		this.socket = sock;
		this.connectionCallback = callback;
		
	}
	
	/**
	 * Tells the loop to stop accepting connections.  This will wait until the next loop timeout.
	 */
	public void finish() {
		this.accepting = false;
	}
	
	@Override
	public void run() {
		
		this.accepting = true;
		
		while (this.accepting) {
			
			try {
				
				Socket added = this.socket.accept();
				
				Nmd.logger.info(String.format("New connection from %s:%s!  Initializing...", added.getInetAddress().getHostAddress(), added.getPort()));
				
				this.connectionCallback.accept(added);
				
			} catch (IOException e) {
				Nmd.logger.finer("Socket accept timed out, no big deal.");
			}
			
		}
		
	}
	
}
