package io.neocore.api.player;

import java.util.Date;
import java.util.function.Consumer;

public interface PlayerLease extends PlayerIdentity {

	/**
	 * Returns the NeoPlayer at whichever state it happens to be in at this
	 * exact moment, it can absolutely be unloaded.
	 * 
	 * @return The player
	 */
	public NeoPlayer getPlayer();

	/**
	 * Blocks until the player has fully loaded and returns it.
	 * 
	 * @return The player
	 * @throws InterruptedException
	 *             if it happens, but probably not
	 */
	public NeoPlayer getPlayerEventually() throws InterruptedException;

	/**
	 * Registers a callback that is invoked after the player has been fully
	 * loaded.
	 * 
	 * @param callback
	 *            The callback to return into
	 */
	public void addCallback(Consumer<NeoPlayer> callback);

	/**
	 * Gets the time that this lease was issued.
	 * 
	 * @return The issue time
	 */
	public Date getIssueTime();

	/**
	 * Releases this lease. If the player has not finished loaded then calling
	 * this method will block until it has. If all leases for the player have
	 * been released, then we will start unloading it.
	 */
	public void release();

	/**
	 * @return If this lease has not been released yet
	 */
	public boolean isValid();

}
