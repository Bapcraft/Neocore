package io.neocore.api.database.ban;

/**
 * A ban issuer that always displays the given string as the "name".
 * 
 * @author treyzania
 */
public class NameLiteralIssuer extends BanIssuer {
	
	private String name;
	
	public NameLiteralIssuer(String name) {
		this.name = name;
	}
	
	@Override
	public String getDisplayName() {
		return this.name;
	}

	@Override
	public String toString() {
		return "explicit:" + this.name;
	}
	
}
