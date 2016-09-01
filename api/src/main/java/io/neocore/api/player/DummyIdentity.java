package io.neocore.api.player;

import java.util.UUID;

/**
 * A "player identity" that is solely defined by the UUID, with no guarante
 * that the UUID corresponds to a real player.
 * 
 * @author treyzania
 */
public class DummyIdentity implements PlayerIdentity {
	
	private UUID uuid;
	
	public DummyIdentity(UUID id) {
		this.uuid = id;
	}
	
	@Override
	public UUID getUniqueId() {
		return this.uuid;
	}

}
