package io.neocore.bukkit.services;

import java.time.Instant;
import java.util.Date;
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
import io.neocore.bukkit.events.PlayerConnectionForwarder;

public class BukkitLoginService implements LoginService {
	
	private LoginAcceptor acceptor;
	private PlayerConnectionForwarder forwarder;
	
	public BukkitLoginService(PlayerConnectionForwarder fwdr) {
		this.forwarder = fwdr;
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
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Session initSession(UUID uuid) {
		
		HostPlugin host = NeocoreAPI.getAgent().getHost();;
		if (host.isFrontServer()) {
			
			NeocoreAPI.getLogger().info("Initing session for " + uuid + "...");
			
			// Initialize the state.
			Player p = Bukkit.getPlayer(uuid);
			Session sess = new Session(p.getUniqueId(), p.getName(), p.getAddress().getAddress(), host.getNeocoreConfig().getServerName());
			sess.setState(SessionState.ACTIVE);
			sess.setStartDate(Date.from(Instant.now()));
			sess.setEndDate(new Date(-1L));
			
			return sess;
			
		}
		
		return null;
		
	}
	
}
