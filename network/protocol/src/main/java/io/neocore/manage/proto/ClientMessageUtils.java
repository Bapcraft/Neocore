package io.neocore.manage.proto;

import java.util.Random;
import java.util.UUID;

import io.neocore.manage.proto.NeomanageProtocol.ClientMessage;

public class ClientMessageUtils {
	
	private static Random rand = new Random();
	
	public static long random() {
		return rand.nextLong();
	}
	
	public static ClientMessage.Builder newBuilder(UUID agentId) {
		
		ClientMessage.Builder builder = ClientMessage.newBuilder();
		builder.setMessageId(random());
		builder.setSenderId(agentId.toString());
		
		return builder;
		
	}
	
}
