package io.neocore.api.database;

import java.util.UUID;

import io.neocore.api.player.IdentityProvider;

public interface IdentityLinkage<T extends PersistentPlayerIdentity> extends IdentityProvider<T> {
	
	/**
	 * Flushes the identity to the database.  Creates records as necessary.
	 * 
	 * @param ident The identity to flush.
	 */
	public void flush(T ident);
	
	/**
	 * Marks cached identities as invalid and reloads new copies from the database.
	 * 
	 * @param ident The newly-reloaded identity.
	 */
	public T reload(UUID uuid);
	
	/**
	 * Marks any cached references invalid and purges them from the caches.
	 * 
	 * @param uuid The UUID of the invalidated record.
	 */
	public void invalidate(UUID uuid);
	
}
