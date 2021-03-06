package io.neocore.bungee.events;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.infrastructure.DownstreamTransferEvent;
import io.neocore.api.infrastructure.NetworkEndpoint;
import io.neocore.api.infrastructure.NetworkMap;
import io.neocore.api.infrastructure.NetworkMapService;
import io.neocore.api.player.NeoPlayer;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.ServerConnectEvent;

public class BungeeDownstreamTransferEvent implements DownstreamTransferEvent {

	private NeoPlayer player;
	private ServerConnectEvent event;

	public BungeeDownstreamTransferEvent(NeoPlayer np, ServerConnectEvent event) {

		this.player = np;
		this.event = event;

	}

	@Override
	public NeoPlayer getPlayer() {
		return this.player;
	}

	@Override
	public void setDestination(NetworkEndpoint dest) {

		if (!NeocoreAPI.getServerName().equals(dest.getNetworkName()))
			throw new IllegalArgumentException("Tried to set an endpoint outside this network.");

		ServerInfo destInfo = ProxyServer.getInstance().getServerInfo(dest.getAgentName());
		if (destInfo != null) {
			NeocoreAPI.getLogger().warning("Tried to set a destination for a player that isn't real.");
		} else {
			this.event.setTarget(destInfo);
		}

	}

	@Override
	public NetworkEndpoint getDestination() {

		String name = this.event.getTarget().getName();

		NetworkMapService nmServ = NeocoreAPI.getAgent().getServiceManager().getService(NetworkMapService.class);
		if (nmServ == null)
			return null; // FIXME TODO
		NetworkMap map = nmServ.getLocalNetworkMap();
		if (map == null)
			return null; // FIXME TODO
		for (NetworkEndpoint ne : map.getEndpoints()) {
			if (ne.getAgentName().equals(name))
				return ne;
		}

		throw new IllegalStateException("Somehow we are going to an unregistered endpoint!");

	}

}
