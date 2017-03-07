package io.neocore.manage.client.net;

import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Preconditions;

import io.neocore.manage.proto.NeomanageProtocol.ClientMessage;
import io.neocore.manage.proto.NeomanageProtocol.ClientMessage.PayloadCase;

public class HandlerManager {

	private Map<PayloadCase, MessageHandler> handlers = new HashMap<>();
	private UnrecognizedHandler unrecog = new UnrecognizedHandler();

	public HandlerManager() {

	}

	public void setHandler(PayloadCase type, MessageHandler hndlr) {
		this.handlers.put(type, hndlr);
	}

	public void handleMessage(NmServer server, ClientMessage msg) {

		// Validation
		Preconditions.checkNotNull(server);
		Preconditions.checkNotNull(msg);

		// Find the handler and then give the message to it.
		MessageHandler hndlr = this.handlers.get(msg.getPayloadCase());
		if (hndlr != null) {
			hndlr.handle(server, msg);
		} else {
			this.unrecog.handle(server, msg);
		}

	}

}
