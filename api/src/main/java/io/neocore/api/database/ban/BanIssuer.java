package io.neocore.api.database.ban;

public abstract class BanIssuer {
	
	protected BanIssuer() {
		// Keep this all to ourselves for now.
	}
	
	public abstract String getDisplayName();
	
}
