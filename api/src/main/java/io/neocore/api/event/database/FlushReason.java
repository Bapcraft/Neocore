package io.neocore.api.event.database;

public enum FlushReason implements DatabaseInteractionReason {
	
	PERIODIC,
	DIRTY_CLEAN,
	EXPLICIT,
	UNLOAD,
	
	OTHER;

	@Override
	public String getAs() {
		return this.name();
	}
	
}
