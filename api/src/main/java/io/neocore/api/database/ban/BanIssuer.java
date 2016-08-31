package io.neocore.api.database.ban;

/**
 * Describes something with a name that can be banned.
 * 
 * @author treyzania
 */
public abstract class BanIssuer {
	
	protected BanIssuer() {
		// Keep this all to ourselves for now.
	}
	
	/**
	 * @return The name of the thing that issued the ban, to be displayed to end-users.
	 */
	public abstract String getDisplayName();
	
}
