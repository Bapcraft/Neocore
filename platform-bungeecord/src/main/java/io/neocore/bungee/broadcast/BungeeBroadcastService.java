package io.neocore.bungee.broadcast;

import io.neocore.api.host.broadcast.BroadcastService;
import net.md_5.bungee.api.ProxyServer;

public class BungeeBroadcastService implements BroadcastService { 
	
	@SuppressWarnings("deprecation")
	@Override
	public void broadcast(String message) {
		
		// Eclipse doesn't want to import BaseCompoenent for me. -n-
		ProxyServer.getInstance().broadcast(message);
		
	}

}
