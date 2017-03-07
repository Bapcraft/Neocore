package io.neocore.api.event.database;

public enum LoadReason implements DatabaseInteractionReason {

	JOIN, REVALIDATE, RELOAD_OTHER,

	OTHER;

	@Override
	public String getAs() {
		return this.name();
	}

}
