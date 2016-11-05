package io.neocore.api.player;

import java.util.UUID;
import java.util.function.Consumer;

import io.neocore.api.ServiceType;

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
	 * Starts initialization of the player in a separate thread.
	 * 
	 * @param uuid The player's UUID.
	 * @param callback A callback once the player has been populated, before events are fired.
	 * @return The player before they are populated.
	 */
	public NeoPlayer startInit(UUID uuid, Consumer<NeoPlayer> callback);
	
	/**
	 * Starts initialization of the player in a separate thread.
	 * 
	 * @param uuid The player's UUID.
	 * @return The player before they are populated.
	 */
	public default NeoPlayer startInit(UUID uuid) {
		return this.startInit(uuid, null);
	}
	
	/**
	 * Triggers asynchronously-loaded identities to be preloaded into the
	 * caches for the given UUID.
	 * 
	 * @param uuid The player's UUID.
	 * @param callback A callback to invoke once complete.
	 */
	public void preload(UUID uuid, Runnable callback);
	
	/**
	 * Triggers asynchronously-loaded identities to be preloaded into the
	 * caches for the given UUID.
	 * 
	 * @param uuid The player's UUID.
	 */
	public default void preload(UUID uuid) {
		this.preload(uuid, () -> {});
	}
	
	/**
	 * Registers a service to be loaded on the player.
	 * 
	 * @param type The service type.
	 */
	public void addService(ServiceType type);
	
}
