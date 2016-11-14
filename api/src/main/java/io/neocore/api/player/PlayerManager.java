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
	 * Triggers asynchronously-loaded identities to be preloaded into the
	 * caches for the given UUID.
	 * 
	 * @param uuid The player's UUID.
	 * @param callback A callback to invoke once complete.
	 */
	public void preload(UUID uuid, Consumer<NeoPlayer> callback);
	
	/**
	 * Triggers asynchronously-loaded identities to be preloaded into the
	 * caches for the given UUID.
	 * 
	 * @param uuid The player's UUID.
	 */
	public default void preload(UUID uuid) {
		this.preload(uuid, o -> {});
	}
	
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
	
}
