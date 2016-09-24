package io.neocore.bukkit.services;

import java.net.InetAddress;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.database.session.Session;
import io.neocore.api.database.session.SessionState;
import io.neocore.api.host.HostPlugin;
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
	public ServerPlayer getPlayer(UUID uuid) {
		
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
		
		Player p = Bukkit.getPlayer(uuid);
		BukkitPlayer bp = new BukkitPlayer(p);
		
		this.players.add(bp);
		return bp;
		
	}
	
	@Override
	public Session initSession(UUID uuid) {
		
		HostPlugin host = NeocoreAPI.getAgent().getHost();;
		if (host.isFrontServer()) {
			
			NeocoreAPI.getLogger().info("Initing session for " + uuid + "...");
			
			// Initialize the state.
			Player p = Bukkit.getPlayer(uuid);
			String name = p.getName();
			InetAddress addr = p
								.getAddress()
								.getAddress();
			String serverName = host.getNeocoreConfig().getServerName();
			
			Session sess = new Session(uuid, name, addr, serverName);
			sess.setState(SessionState.ACTIVE);
			sess.setStartDate(Date.from(Instant.now()));
			sess.setEndDate(new Date(-1L));
			
			return sess;
			
		}
		
		return null;
		
	}
	
}
