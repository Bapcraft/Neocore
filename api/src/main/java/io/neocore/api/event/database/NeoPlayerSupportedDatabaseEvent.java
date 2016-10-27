package io.neocore.api.event.database;

import java.util.UUID;

import io.neocore.api.player.NeoPlayer;

public abstract class NeoPlayerSupportedDatabaseEvent<R extends DatabaseInteractionReason> extends DatabaseIdentityEvent<R> {
	
	private NeoPlayer player;
	
	public NeoPlayerSupportedDatabaseEvent(R reason, NeoPlayer player) {
		
		super(reason);
		
		this.player = player;
		
	}
	
	public NeoPlayer getPlayer() {
		return this.player;
	}
	
	@Override
	public UUID getUniqueId() {
		return this.getPlayer().getUniqueId();
	}
	
}
