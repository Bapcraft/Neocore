package io.neocore.api.player;

import java.util.UUID;

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
