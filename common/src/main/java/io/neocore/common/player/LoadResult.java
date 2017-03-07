package io.neocore.common.player;

import io.neocore.api.player.PlayerIdentity;

public final class LoadResult extends ActionResult {
	
	private PlayerIdentity identity;
	private Exception exception;
	
	public LoadResult(ActionStatus status, PlayerIdentity loaded) {
		
		super(status);
		
		this.identity = loaded;
		
	}
	
	public LoadResult(ActionStatus status, Exception e) {
		
		super(status);
		
		this.exception = e;
		
	}
	
	public PlayerIdentity getLoadedIdentity() {
		return this.identity;
	}
	
	public Exception getException() {
		return this.exception;
	}
	
}
