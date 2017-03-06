package io.neocore.manage.client.channel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import com.treyzania.jzania.ExoContainer;

import io.neocore.api.Neocore;
import io.neocore.api.NeocoreAPI;
import io.neocore.api.infrastructure.NetMessage;
import io.neocore.api.infrastructure.NetworkChannelService;
import io.neocore.manage.client.net.NmNetwork;
import io.neocore.manage.proto.ClientMessageUtils;
import io.neocore.manage.proto.NeomanageProtocol.ChannelBroadcast;
import io.neocore.manage.proto.NeomanageProtocol.ClientMessage;

public class NmdNetworkChannelService implements NetworkChannelService {
	
	private Neocore neocore;
	private NmNetwork network;
	private ExoContainer container = new ExoContainer(NeocoreAPI.getLogger());
	
	private Map<String, List<Consumer<NetMessage>>> channelHandlers = new HashMap<>();
	private List<Consumer<NetMessage>> generalHandlers = new ArrayList<>();
	
	@Override
	public void queueMessage(String channel, String message, Runnable onSend) {
		
		ClientMessage.Builder b = ClientMessageUtils.newBuilder(this.neocore.getAgentId());
		b.setChannelBroadcast(ChannelBroadcast.newBuilder().setChannel(channel).setPayload(message));
		
		this.network.getActiveServer().queueMessage(b.build());
		
	}

	@Override
	public void registerChannelHandler(String channel, Consumer<NetMessage> handler) {
		
		List<Consumer<NetMessage>> handlers = this.channelHandlers.get(channel);
		if (handlers == null) {
			
			handlers = new ArrayList<>();
			this.channelHandlers.put(channel, handlers);
			
		}
		
		handlers.add(handler);
		
	}

	@Override
	public void registerGeneralHandler(Consumer<NetMessage> handler) {
		this.generalHandlers.add(handler);
	}

	@Override
	public void unregisterHandler(Consumer<NetMessage> handler) {
		
		this.generalHandlers.remove(handler);
		for (List<Consumer<NetMessage>> chHandlers : this.channelHandlers.values()) {
			chHandlers.remove(handler);
		}
		
	}
	
	public void broadcast(NetMessage message) {
		
		// First do the general handlers as they have higher priority.
		for (Consumer<NetMessage> genHandler : this.generalHandlers) {
			this.container.invoke("Nmd.NetMessage", () -> genHandler.accept(message));
		}
		
		// Now find the channel handlers.
		for (Consumer<NetMessage> chHandler : this.channelHandlers.get(message.getChannelName())) {
			this.container.invoke("Nmd.NetMessage", () -> chHandler.accept(message));
		}
		
	}
	
}
