package io.neocore.common.player;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.logging.Level;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.RegisteredService;
import io.neocore.api.ServiceManager;
import io.neocore.api.ServiceProvider;
import io.neocore.api.ServiceType;
import io.neocore.api.database.DatabaseService;
import io.neocore.api.database.LoadAsync;
import io.neocore.api.database.player.DatabasePlayer;
import io.neocore.api.database.player.PlayerService;
import io.neocore.api.database.session.Session;
import io.neocore.api.database.session.SessionService;
import io.neocore.api.event.database.FlushReason;
import io.neocore.api.event.database.LoadReason;
import io.neocore.api.host.HostService;
import io.neocore.api.host.Scheduler;
import io.neocore.api.host.login.LoginService;
import io.neocore.api.player.IdentityProvider;
import io.neocore.api.player.NeoPlayer;
import io.neocore.api.player.PlayerIdentity;

public class CommonPlayerManager {
	
	private Set<NeoPlayer> playerCache = new TreeSet<>();
	
	private ServiceManager serviceManager;
	private Scheduler scheduler;
	
	private DataServiceWrapper<DatabasePlayer, PlayerService> playerWrapper;
	private DataServiceWrapper<Session, SessionService> sessionWrapper;
	private boolean wrappedOk = false;
	
	public CommonPlayerManager(ServiceManager sm, Scheduler sched) {
		
		this.serviceManager = sm;
		this.scheduler = sched;
		
	}
	
	public NeoPlayer getPlayer(UUID uuid) {
		
		// Try to find the player if it's there already...
		for (NeoPlayer np : this.playerCache) {
			if (np.getUniqueId().equals(uuid)) return np;
		}
		
		// Don't assemble a new one.  That shouldn't be allowed except after post logins.
		return null;
		
	}
	
	public synchronized NeoPlayer assemblePlayer(UUID uuid, Consumer<NeoPlayer> callback) {
		
		NeoPlayer np = new NeoPlayer(uuid);
		
		// TODO Load the simple subsystems.
		
		// Make sure these are all good to go.
		this.initWrappers();
		
		// Set up the data load.
		this.playerWrapper.load(uuid, LoadReason.JOIN, dbp -> {
			
			try {
				copyIntoField(np, dbp);
			} catch (Throwable t) {
				NeocoreAPI.getLogger().log(Level.SEVERE, "Problem injecting player data for player " + uuid + "!", t);
			}
			
			// TODO Configure permissions based on the group records.
			
		});
		
		// Load the session, either by creating it or loading it from the database.
		if (NeocoreAPI.isFrontend()) {
			
			// We have to initialize it directly because they are *probably* connecting directly.
			LoginService login = this.serviceManager.getService(LoginService.class);
			Session inited = login.initSession(uuid);
			
			try {
				copyIntoField(np, inited);
			} catch (Throwable t) {
				NeocoreAPI.getLogger().log(Level.SEVERE, "Problem injecting new session data for player " + uuid + "!", t);
			}
			
			// Now we wait to finish flushing before we continue.
			CountDownLatch flushReturn = new CountDownLatch(1);
			this.sessionWrapper.flush(inited, FlushReason.EXPLICIT, () -> flushReturn.countDown()); // TODO Events should know that it's because it's a new object.
			try {
				flushReturn.await(5L, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				NeocoreAPI.getLogger().severe("Took too long to flush newly-created session data for player " + uuid + "!");
			}
			
		} else {
			
			// In this case we just have to load it from the DB because we know someone else made it.
			this.sessionWrapper.load(uuid, LoadReason.JOIN, sess -> {
				
				try {
					copyIntoField(np, sess);
				} catch (Throwable t) {
					NeocoreAPI.getLogger().log(Level.SEVERE, "Problem injecting session data for player " + uuid + "!", t);
				}
				
			});
			
		}
		
		// Done.
		this.playerCache.add(np);
		if (callback != null) callback.accept(np);
		return np;
		
	}
	
	public NeoPlayer assemblePlayer(UUID uuid) {
		return this.assemblePlayer(uuid, null);
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	private void getAndInject(ServiceType type, NeoPlayer player) {
		
		RegisteredService reg = this.serviceManager.getService(type);
		if (reg == null) return;
		
		ServiceProvider prov = reg.getServiceProvider();
		if (!(prov instanceof IdentityProvider)) return;
		
		IdentityProvider<? extends PlayerIdentity> ip = (IdentityProvider<? extends PlayerIdentity>) prov;
		
		Runnable injector = () -> {
			
			PlayerIdentity pi = ip.getPlayer(player.getUniqueId());
			
			try {
				copyIntoField(player, pi);
			} catch (Exception e) {
				NeocoreAPI.getLogger().log(Level.SEVERE, "Error injecting player aspect " + reg.getType().getName() + "!", e);
			}
			
		};
		
		// Run it in a separate thread if we need to, otherwise run it here.
		if (prov.getClass().isAnnotationPresent(LoadAsync.class)) {
			this.scheduler.invokeAsync(injector);
		} else {
			injector.run();
		}
		
	}
	
	private static Field findField(Class<?> clazz, Class<?> type) {
		
		for (Field f : clazz.getDeclaredFields()) {
			if (f.getType().isAssignableFrom(type)) return f;
		}
		
		return null;
		
	}
	
	private static void copyIntoField(Object container, Object into) throws IllegalArgumentException, IllegalAccessException {
		
		Field f = findField(container.getClass(), into.getClass());
		
		boolean acc = f.isAccessible();
		f.setAccessible(true);
		f.set(container, into);
		f.setAccessible(acc);
		
	}
	
	public synchronized void unloadPlayer(UUID uuid) {
		
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
				ip.unload(np.getUniqueId());
				
			}
			
		}
		
		this.playerCache.removeIf(p -> p.getUniqueId().equals(uuid));
		
	}
	
	public void unloadPlayer(PlayerIdentity pi) {
		this.unloadPlayer(pi.getUniqueId());
	}
	
	private synchronized void initWrappers() {

		if (this.wrappedOk) return;
		
		this.playerWrapper = new DataServiceWrapper<>(this.scheduler, null, this.serviceManager.getService(PlayerService.class));
		this.sessionWrapper = new DataServiceWrapper<>(this.scheduler, null, this.serviceManager.getService(SessionService.class));
		this.wrappedOk = true;
		
	}
	
	private static List<ServiceType> getInjectedServices() {
		
		List<ServiceType> types = new ArrayList<>();
		types.addAll(Arrays.asList(
			HostService.LOGIN,
			HostService.PERMISSIONS,
			HostService.CHAT,
			HostService.PROXY,
			HostService.ENDPOINT));
		
		return types;
		
	}
	
}
