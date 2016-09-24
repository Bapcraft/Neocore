package io.neocore.common.player;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.logging.Level;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.RegisteredService;
import io.neocore.api.ServiceManager;
import io.neocore.api.ServiceProvider;
import io.neocore.api.ServiceType;
import io.neocore.api.database.DatabaseService;
import io.neocore.api.database.player.DatabasePlayer;
import io.neocore.api.database.session.Session;
import io.neocore.api.database.session.SessionService;
import io.neocore.api.host.HostService;
import io.neocore.api.host.login.LoginService;
import io.neocore.api.player.IdentityProvider;
import io.neocore.api.player.NeoPlayer;
import io.neocore.api.player.PlayerIdentity;
import io.neocore.common.HostPlayerInjector;

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
	
	public NeoPlayer assemblePlayer(UUID uuid) {
		
		NeoPlayer np = new NeoPlayer(uuid);
		
		// TODO Do something with the supplier this generate
		this.hostPlayerInjector.injectPermissions(np);
		
		injectFields(np);
		
		// Done.
		this.playerCache.add(np);
		return np;
		
	}
	
	private void injectFields(NeoPlayer player) {
		
		// Tabulate the services we need player data from.
		List<ServiceType> idents = getInjectedServices();
		
		// Get the actual objects we need to inject.
		List<Object> injections = new ArrayList<>();
		for (ServiceType type : idents) {
			
			RegisteredService reg = this.serviceManager.getService(type);
			if (reg == null) continue;
			
			ServiceProvider prov = reg.getServiceProvider();
			
			NeocoreAPI.getLogger().info("Pooling " + prov.getClass().getName() + " for injection...");
			
			// We need some speical handling for sessions.
			if (prov instanceof IdentityProvider) injections.add(((IdentityProvider<?>) prov).getPlayer(player));
			
		}
		
		// Now figure out the session bullshit.
		SessionService serv = this.serviceManager.getService(SessionService.class);
		if (NeocoreAPI.getAgent().getHost().isFrontServer()) {
			
			// We have to initialize the session.
			LoginService login = this.serviceManager.getService(LoginService.class);
			Session sess = login.initSession(player.getUniqueId());
			serv.flush(sess);
			injections.add(sess);
			
		} else {
			injections.add(serv.getSession(player.getUniqueId()));
		}
		
		// Actually inject the things.
		for (Object o : injections) {
			
			if (o == null) continue;
			
			try {
				
				for (Field f : player.getClass().getDeclaredFields()) {
					
					if (f.getType().isAssignableFrom(o.getClass())) {
						
						boolean acc = f.isAccessible();
						f.setAccessible(true);
						f.set(player, o);
						f.setAccessible(acc);
						
					}
					
				}
				
			} catch (Exception e) {
				NeocoreAPI.getLogger().log(Level.WARNING, "Error injecting player aspect " + o.getClass().getSimpleName() + "!", e);
			}
			
		}
		
		// Now update the username.
		LoginService login = this.serviceManager.getService(LoginService.class);
		DatabasePlayer dbp = player.getIdentity(DatabasePlayer.class);
		String lastUsername = dbp.getLastUsername();
		String currentUsername = login.getPlayer(player).getName();
		if (!lastUsername.equals(currentUsername)) dbp.setLastUsername(currentUsername);
		
	}
	
	public void unloadPlayer(UUID uuid) {
		
		NeoPlayer np = this.getPlayer(uuid);
		NeocoreAPI.getLogger().info(uuid + " -> " + np);
		
		// Flush the session data.
		List<ServiceType> servs = getInjectedServices();
		servs.add(DatabaseService.SESSION);
		
		for (ServiceType type : servs) {
			
			RegisteredService reg = this.serviceManager.getService(type);
			if (reg == null) continue;
			
			ServiceProvider prov = reg.getServiceProvider();
			
			if (prov instanceof IdentityProvider<?>) {
				
				IdentityProvider<?> ip = (IdentityProvider<?>) prov;
				
				// Quick and easy, should flush things if necessary.
				ip.unload(np);
				
			}
			
		}
		
		this.playerCache.removeIf(p -> p.getUniqueId().equals(uuid));
		
	}
	
	public void unloadPlayer(PlayerIdentity pi) {
		this.unloadPlayer(pi.getUniqueId());
	}
	
	private static List<ServiceType> getInjectedServices() {
		
		List<ServiceType> types = new ArrayList<>();
		types.addAll(Arrays.asList(
			HostService.LOGIN,
			HostService.PERMISSIONS,
			HostService.CHAT,
			DatabaseService.PLAYER,
			HostService.PROXY,
			HostService.ENDPOINT));
		
		return types;
		
	}
	
}
