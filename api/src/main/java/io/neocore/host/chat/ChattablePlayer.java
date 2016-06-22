package io.neocore.host.chat;

import io.neocore.player.PlayerIdentity;

public interface ChattablePlayer extends PlayerIdentity {
	
	public void setDisplayName(String name);
	public String getDisplayName();
	
	public void sendMessage(String message);
	
}
