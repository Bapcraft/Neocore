package io.neocore.player;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.logging.Level;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.ServiceManager;
import io.neocore.api.host.chat.ChatProvider;
import io.neocore.api.host.chat.ChattablePlayer;
import io.neocore.api.host.login.LoginProvider;
import io.neocore.api.host.login.ServerPlayer;
import io.neocore.api.host.permissions.PermissedPlayer;
import io.neocore.api.host.permissions.PermissionsProvider;
import io.neocore.api.host.proxy.NetworkParticipant;
import io.neocore.api.host.proxy.NetworkPlayer;
import io.neocore.api.player.NeoPlayer;
import io.neocore.api.player.PlayerIdentity;

public class PlayerManager {
	
	private Set<NeoPlayer> playerCache = new TreeSet<>();
	
	private ServiceManager serviceManager;
	
	public PlayerManager(ServiceManager sm) {
		
		this.serviceManager = sm;
		
	}
	
	public NeoPlayer getPlayer(UUID uuid) {
		
		// Try to find the player if it's there already...
		for (NeoPlayer np : this.playerCache) {
			if (np.getUniqueId().equals(uuid)) return np;
		}
		
		// Otherwise assemble a new one.
		return this.assemblePlayer(uuid);
		
	}
	
	public NeoPlayer assemblePlayer(UUID uuid) {
		
		NeoPlayer np = new NeoPlayer(uuid);
		
		ServerPlayer server = this.serviceManager.getService(LoginProvider.class).getPlayer(uuid);
		NetworkPlayer network = this.serviceManager.getService(NetworkParticipant.class).getPlayer(uuid);
		PermissedPlayer permissions = this.serviceManager.getService(PermissionsProvider.class).getPlayer(uuid);
		ChattablePlayer chat = this.serviceManager.getService(ChatProvider.class).getPlayer(uuid);
		
		// XXX Messy but quick way to inject these objects.  Should I be using Guice at this point?
		List<Object> injections = new ArrayList<>();
		injections.add(server);
		injections.add(network);
		injections.add(permissions);
		injections.add(chat);
		for (Object o : injections) {
			
			if (o == null) continue;
			
			try {
				
				for (Field f : np.getClass().getDeclaredFields()) {
					
					if (f.getType().isAssignableFrom(o.getClass())) {
						
						boolean acc = f.isAccessible();
						f.setAccessible(true);
						f.set(np, o);
						f.setAccessible(acc);
						
					}
					
				}
				
			} catch (Exception e) {
				NeocoreAPI.getLogger().log(Level.WARNING, "Error injecting player aspect " + o.getClass().getSimpleName() + "!", e);
			}
			
		}
		
		this.playerCache.add(np);
		return np;
		
	}
	
	public void unloadPlayer(UUID uuid) {
		this.playerCache.removeIf(p -> p.getUniqueId().equals(uuid));
	}
	
	public void unloadPlayer(PlayerIdentity pi) {
		this.unloadPlayer(pi.getUniqueId());
	}
	
}
