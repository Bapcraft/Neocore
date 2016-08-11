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
import io.neocore.api.host.HostPlayerInjector;
import io.neocore.api.host.chat.ChatService;
import io.neocore.api.host.chat.ChattablePlayer;
import io.neocore.api.host.login.LoginService;
import io.neocore.api.host.login.ServerPlayer;
import io.neocore.api.host.permissions.PermissedPlayer;
import io.neocore.api.host.permissions.PermissionsService;
import io.neocore.api.host.proxy.NetworkParticipant;
import io.neocore.api.host.proxy.NetworkPlayer;
import io.neocore.api.player.NeoPlayer;
import io.neocore.api.player.PlayerIdentity;

public class CommonPlayerManager {
	
	private Set<NeoPlayer> playerCache = new TreeSet<>();
	
	private ServiceManager serviceManager;
	private HostPlayerInjector hostPlayerInjector;
	
	public CommonPlayerManager(ServiceManager sm, HostPlayerInjector injector) {
		
		this.serviceManager = sm;
		this.hostPlayerInjector = injector;
		
	}
	
	public NeoPlayer getPlayer(UUID uuid) {
		
		// Try to find the player if it's there already...
		for (NeoPlayer np : this.playerCache) {
			if (np.getUniqueId().equals(uuid)) return np;
		}
		
		// Don't assemble a new one.  That shouldn't be allowed except after post logins.
		return null;
		
	}
	
	protected NeoPlayer assemblePlayer(UUID uuid) {
		
		NeoPlayer np = new NeoPlayer(uuid);
		
		ServerPlayer server = this.serviceManager.getService(LoginService.class).getPlayer(uuid);
		NetworkPlayer network = this.serviceManager.getService(NetworkParticipant.class).getPlayer(uuid);
		PermissedPlayer permissions = this.serviceManager.getService(PermissionsService.class).getPlayer(uuid);
		ChattablePlayer chat = this.serviceManager.getService(ChatService.class).getPlayer(uuid);
		
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
		
		// TODO Do something with the supplier this generate
		this.hostPlayerInjector.injectPermissions(np);
		
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
