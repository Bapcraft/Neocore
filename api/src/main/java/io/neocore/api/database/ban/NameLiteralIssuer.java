package io.neocore.api.database.ban;

public class NameLiteralIssuer extends BanIssuer {
	
	private String name;
	
	public NameLiteralIssuer(String name) {
		this.name = name;
	}
	
	@Override
	public String getDisplayName() {
		return this.name;
	}
	
}
