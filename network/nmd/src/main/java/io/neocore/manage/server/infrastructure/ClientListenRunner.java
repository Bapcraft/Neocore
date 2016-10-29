package io.neocore.manage.server.infrastructure;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.logging.Level;

import io.neocore.manage.proto.NeomanageProtocol.ClientMessage;
import io.neocore.manage.server.Nmd;
import io.neocore.manage.server.handling.HandlerRunner;
import io.neocore.manage.server.handling.MessageHandler;

public class ClientListenRunner extends HandlerRunner {
	
	private DaemonServer server;
	private NmClient client;
	
	private DataInputStream inputStream;
	
	public ClientListenRunner(NmClient cli, DaemonServer serv) {
		
		this.client = cli;
		this.server = serv;
		
		try {
			this.inputStream = new DataInputStream(client.socket.getInputStream());
		} catch (IOException e) {
			
			Nmd.logger.log(
				Level.SEVERE,
				String.format("Could not open loop thread for %s (%s)!  Closing...", client.getAddressString(), client.getIdentString()),
				e
			);
			
			try {
				client.socket.close();
			} catch (IOException e2) {
				Nmd.logger.log(Level.WARNING, "Failure in closing connection!", e2);
			}
			
		}
		
	}
	
	@Override
	public void cycle() {
		
		try {
			
			ClientMessage message = ClientMessage.parseFrom(this.inputStream);
			
			// Now just find the message and forward the handling.
			MessageHandler h = this.server.getMessageManager().getHandle(message.getMessageCase());
			h.handle(this.server, this.client, message);
			
		} catch (IOException e) {
			Nmd.logger.log(Level.WARNING, "Problem parsing message from " + this.client.getIdentString() + ".", e);
		}
		
	}
	
}
