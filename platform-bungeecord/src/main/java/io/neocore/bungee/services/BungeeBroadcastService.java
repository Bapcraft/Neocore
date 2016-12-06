package io.neocore.bungee.services;

import io.neocore.api.host.broadcast.BroadcastService;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;

public class BungeeBroadcastService implements BroadcastService { 
	
	private ProxyServer bungee;
	
	public BungeeBroadcastService(ProxyServer bungee) {
		this.bungee = bungee;
	}
	
	@Override
	public void broadcast(String message) {
		this.bungee.broadcast(new TextComponent(ChatColor.translateAlternateColorCodes('&', message)));
	}
	
}
