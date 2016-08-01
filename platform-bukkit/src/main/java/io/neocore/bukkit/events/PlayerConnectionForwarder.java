package io.neocore.bukkit.events;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.host.login.LoginAcceptor;
import io.neocore.api.player.NeoPlayer;
import io.neocore.bukkit.events.wrappers.BukkitInitialLoginEvent;
import io.neocore.bukkit.events.wrappers.BukkitPostJoinEvent;
import io.neocore.bukkit.events.wrappers.BukkitQuitEvent;

public class PlayerConnectionForwarder extends EventForwarder {
	
	public LoginAcceptor acceptor;
	
	public void onPlayerLogin(PlayerLoginEvent event) {
		
		BukkitInitialLoginEvent neoEvent = new BukkitInitialLoginEvent(event);
		this.acceptor.onInitialLoginEvent(neoEvent);
		
	}
	
	public void onPlayerJoin(PlayerJoinEvent event) {
		
		Player p = event.getPlayer();
		NeoPlayer np = NeocoreAPI.getAgent().getPlayer(p.getUniqueId());
		
		BukkitPostJoinEvent neoEvent = new BukkitPostJoinEvent(event, np);
		this.acceptor.onPostLoginEvent(neoEvent);
		
	}
	
	public void onPlayerQuit(PlayerQuitEvent event) {
		
		NeoPlayer np = NeocoreAPI.getAgent().getPlayer(event.getPlayer().getUniqueId());
		BukkitQuitEvent neoEvent = new BukkitQuitEvent(event, np);
		this.acceptor.onDisconnectEvent(neoEvent);
		
	}
	
}
