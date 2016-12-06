package io.neocore.bukkit.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import io.neocore.api.ServiceManager;
import io.neocore.api.host.login.LoginAcceptor;
import io.neocore.api.host.login.LoginService;
import io.neocore.api.host.login.ServerPlayer;
import io.neocore.bukkit.BukkitPlayer;
import io.neocore.bukkit.events.PlayerConnectionForwarder;

public class BukkitLoginService implements LoginService {
	
	private LoginAcceptor acceptor;
	private PlayerConnectionForwarder forwarder;
	
	private List<BukkitPlayer> players;
	
	public BukkitLoginService(PlayerConnectionForwarder fwdr) {
		
		this.forwarder = fwdr;
		this.players = new ArrayList<>();
		
	}
	
	@Override
	public void setLoginAcceptor(LoginAcceptor acceptor) {
		
		this.acceptor = acceptor;
		this.forwarder.acceptor = acceptor;
		
	}
	
	@Override
	public LoginAcceptor getLoginAcceptor() {
		return this.acceptor;
	}
	
	@Override
	public ServerPlayer load(UUID uuid) {
		
		// Initialize one way or another.
		BukkitPlayer bp = this.findPlayer(uuid);
		if (bp == null) bp = this.initPlayer(uuid);
		
		return bp;
		
	}
	
	private BukkitPlayer findPlayer(UUID uuid) {
		
		// If we can't find it then just return null.
		for (BukkitPlayer p : this.players) {
			if (p.getUniqueId().equals(uuid)) return p;
		}
		
		return null;
		
	}
	
	private BukkitPlayer initPlayer(UUID uuid) {
		
		BukkitPlayer bp = new BukkitPlayer(Bukkit.getPlayer(uuid));
		this.players.add(bp);
		return bp;
		
	}
	
}
