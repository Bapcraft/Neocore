package io.neocore.manage.client.net;

import io.neocore.manage.proto.NeomanageProtocol.ClientMessage;

public abstract class MessageHandler {

	public abstract void handle(NmServer sender, ClientMessage message);

}
