package io.neocore.bungee.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.neocore.api.host.login.LoginAcceptor;
import io.neocore.api.host.login.LoginService;
import io.neocore.api.host.login.ServerPlayer;
import io.neocore.bungee.events.PlayerConnectionForwarder;
import net.md_5.bungee.api.ProxyServer;

public class BungeeLoginService implements LoginService {
	
	private PlayerConnectionForwarder forwarder;
	
	private List<BungeePlayer> cache = new ArrayList<>();
	
	public BungeeLoginService(PlayerConnectionForwarder fwdr) {
		this.forwarder = fwdr;
	}

	@Override
	public ServerPlayer load(UUID uuid) {
		
		// Initialize one way or another.
		BungeePlayer bp = this.findPlayer(uuid);
		if (bp == null) bp = this.initPlayer(uuid);
		
		return bp;
		
	}
	
	private BungeePlayer findPlayer(UUID uuid) {
		
		// If we can't find it then just return null.
		for (BungeePlayer p : this.cache) {
			if (p.getUniqueId().equals(uuid)) return p;
		}
		
		return null;
		
	}
	
	private BungeePlayer initPlayer(UUID uuid) {
		
		BungeePlayer bp = new BungeePlayer(ProxyServer.getInstance().getPlayer(uuid));
		this.cache.add(bp);
		return bp;
		
	}
	
	@Override
	public void unload(UUID uuid) {
		this.cache.removeIf(p -> p.getUniqueId().equals(uuid));
	}
	
	@Override
	public void setLoginAcceptor(LoginAcceptor acceptor) {
		this.forwarder.loginAcceptor = acceptor;
	}
	
	@Override
	public LoginAcceptor getLoginAcceptor() {
		return this.forwarder.loginAcceptor;
	}
	
}
