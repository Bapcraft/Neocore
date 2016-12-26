package io.neocore.api.player;

import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

import io.neocore.api.ServiceType;
import io.neocore.api.event.database.PostLoadPlayerEvent;

public interface PlayerManager {
	
	/**
	 * Checks to see if the player has been inited in the player manager,
	 * however this does not mean that the player has been fully populated, but
	 * they are likely to be soon.
	 * 
	 * @see isPopulated(UUID)
	 * @see PostLoadPlayerEvent
	 * 
	 * @param uuid The UUID of the player.
	 * @return If the player has been initialized.
	 */
	public boolean isInited(UUID uuid);
	
	/**
	 * Checks to see if the player has all of its initial identities populated
	 * in the player.
	 * 
	 * @param uuid The UUID of the player.
	 * @return If the player has been fully populated.
	 */
	public boolean isPopulated(UUID uuid);
	
	/**
	 * Returns the NeoPlayer instance for the player.
	 * 
	 * @param uuid The UUID of the player.
	 * @return The player, or <code>null</code> if not available.
	 */
	public NeoPlayer getPlayer(UUID uuid);
	
	/**
	 * Registers a service to be loaded on the player.
	 * 
	 * @param type The service type.
	 */
	public void addService(ServiceType type);
	
	/**
	 * Returns a set of the online players.  Manipulations to this set will
	 * have no effect on the players online.
	 * 
	 * @return A set of the online players.
	 */
	public Set<NeoPlayer> getOnlinePlayers();
	
	/**
	 * Requests a lease for the specified player.  If there are no leases for
	 * the player then it will trigger a low-level load for the player data, if
	 * another lease is requested after this then nothing will happen.  If we
	 * have begun to unload the player and a lease is requested then we will
	 * wait until we have finished unloading it to start loading it again.
	 * 
	 * @param uuid The player's UUID
	 * @return The lease for this player.
	 */
	public PlayerLease requestLease(UUID uuid);
	
	/**
	 * Does the same as the function with only one argument, but registers the
	 * callback before triggering anything.
	 * 
	 * @see requestLease(UUID)
	 * @param uuid The UUID of the player
	 * @param callback Callback to invoke once the player has finished loading
	 * @return
	 */
	public PlayerLease requestLease(UUID uuid, Consumer<NeoPlayer> callback);
	
}
