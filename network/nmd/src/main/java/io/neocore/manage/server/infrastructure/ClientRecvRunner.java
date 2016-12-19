package io.neocore.manage.server.infrastructure;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;

import io.neocore.manage.proto.NeomanageProtocol.ClientMessage;
import io.neocore.manage.server.Nmd;
import io.neocore.manage.server.handling.HandlerRunner;
import io.neocore.manage.server.handling.MessageHandler;

public class ClientRecvRunner extends HandlerRunner {
	
	private DaemonServer server;
	private NmClient client;
	
	private InputStream stream;
	
	public ClientRecvRunner(NmClient cli, DaemonServer serv, InputStream is) {
		
		this.client = cli;
		this.server = serv;
		
		this.stream = is;
		
	}
	
	@Override
	public void cycle() {
		
		try {
			
			// Get message, then just pass it along to the handlers.
			ClientMessage message = ClientMessage.parseDelimitedFrom(this.stream);
			this.client.update(); // Update timeout info.
			Nmd.logger.fine("Recieved from " + this.client.name + " ID:" + message.getMessageId() + " of type " + message.getPayloadCase() + ".");
			
			// Now just find the message and forward the handling.
			List<MessageHandler> hs = this.server.getMessageManager().getHandlers(message.getPayloadCase());
			hs.forEach(h -> h.handle(this.server, this.client, message));
			
		} catch (IOException e) {
			
			if (this.client.isConnected()) {
				
				Nmd.logger.log(Level.SEVERE, "Problem parsing message from " + this.client.getIdentString() + ".", e);
				this.client.forceDisconnect();
				this.disable();
				
			}
			
		}
		
	}
	
}
