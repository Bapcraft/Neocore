package io.neocore.api.database;

import java.io.IOException;
import java.util.UUID;

import io.neocore.api.player.IdentityProvider;

public interface IdentityLinkage<T extends PersistentPlayerIdentity> extends IdentityProvider<T> {

	/**
	 * Flushes the corresponding identity to the database. Creates records as
	 * necessary.
	 * 
	 * @param uuid
	 *            The uuid of the identity to flush.
	 * @throws IOException
	 *             TODO
	 */
	public void flush(UUID uuid) throws IOException;

	/**
	 * Marks any cached references invalid and purges them from the caches.
	 * 
	 * @param uuid
	 *            The UUID of the invalidated record.
	 */
	public void invalidate(UUID uuid);

}
