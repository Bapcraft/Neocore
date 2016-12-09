package io.neocore.manage.client;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;

import io.neocore.api.NeocoreAPI;

public class NmServer implements Closeable {
	
	private Socket socket;
	
	public NmServer(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public synchronized void close() {
		
		try {
			this.socket.close();
		} catch (IOException e) {
			NeocoreAPI.getLogger().log(Level.SEVERE, "Problem when closing socket.", e);
		}
		
	}
	
	public boolean isOnline() {
		return !this.socket.isClosed();
	}
	
	public void write(byte[] data) throws IOException {
		this.socket.getOutputStream().write(data);
	}
	
}
