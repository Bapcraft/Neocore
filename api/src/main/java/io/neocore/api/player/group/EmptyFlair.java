package io.neocore.api.player.group;

public class EmptyFlair implements Flair {
	
	@Override
	public void setPrefix(String prefix) {
		
	}
	
	@Override
	public String getPrefix() {
		return "";
	}
	
	@Override
	public void setSuffix(String suffix) {
		
	}
	
	@Override
	public String getSuffix() {
		return "";
	}
	
	@Override
	public String apply(String playerName) {
		return playerName;
	}
	
}
