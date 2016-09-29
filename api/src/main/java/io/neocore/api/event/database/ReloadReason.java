package io.neocore.api.event.database;

public enum ReloadReason implements DatabaseInteractionReason {
	
	INVALIDATION,
	EXPLICIT,
	
	OTHER;

	@Override
	public String getAs() {
		return this.name();
	}
	
}
