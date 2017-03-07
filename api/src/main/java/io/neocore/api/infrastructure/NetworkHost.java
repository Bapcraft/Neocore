package io.neocore.api.infrastructure;

import java.util.Set;
import java.util.UUID;

public interface NetworkHost extends Networkable {

	/**
	 * Returns a set of players registered by this network member. This can
	 * change at any time.
	 * 
	 * @return The players on this member
	 */
	public Set<NetworkPlayer> getPlayers();

	/**
	 * Returns <code>true</code> if this endpoint has the player with the UUID
	 * in question.
	 * 
	 * @param uuid
	 *            The player's UUID
	 * @return The membership state.
	 */
	public default boolean hasPlayer(UUID uuid) {

		for (NetworkPlayer np : this.getPlayers()) {
			if (np.getUniqueId().equals(uuid))
				return true;
		}

		return false;

	}

}
