package io.neocore.api.database.ban;

import java.util.List;
import java.util.UUID;

import io.neocore.api.database.DatabaseServiceProvider;

public interface BanService extends DatabaseServiceProvider {

	/**
	 * Gets the list of bans for the given player.
	 * 
	 * @param uuid
	 *            The UUID of the player.
	 * @return The player's ban list.
	 */
	public List<BanEntry> getBans(UUID uuid);

	/**
	 * Clears and loads the local cache of bans from the database.
	 */
	public void reloadBans();

	/**
	 * Flushes any dirty bans to the backend.
	 */
	public void flushBans();

	/**
	 * Adds the ban to the database and updates it in the relevant local cache.
	 * 
	 * @param entry
	 */
	public void addBan(BanEntry entry);

	/**
	 * Creates a new native ban for the specified ID than can be used directly
	 * with this service. Does not add the ban to the official record yet.
	 * 
	 * @param uuid
	 *            The player's UUID.
	 * @return The ban entry.
	 */
	public BanEntry createNewBan(UUID uuid);

}
