package io.neocore.api.event.database;

public enum UnloadReason implements DatabaseInteractionReason {

	DISCONNECT,

	OTHER;

	@Override
	public String getAs() {
		return this.name();
	}

}
