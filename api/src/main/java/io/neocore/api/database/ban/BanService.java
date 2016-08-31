package io.neocore.api.database.ban;

import java.util.UUID;

import io.neocore.api.database.DatabaseServiceProvider;

public interface BanService extends DatabaseServiceProvider {
	
	/**
	 * Gets the list of bans for the given player.
	 * 
	 * @param uuid The UUID of the player.
	 * @return The player's ban list.
	 */
	public BanList getBans(UUID uuid);
	
	/**
	 * Adds a new ban record to the database.
	 * @param entry The ban entry to be added.
	 */
	public void addBan(BanEntry entry);
	
	/**
	 * Purges all previous bans for the player and adds in the bans from the list to the database.
	 * 
	 * @param list The list to use as the "current" for the list's "owner".
	 */
	public void updateBanList(BanList list);
	
}
