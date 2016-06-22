package io.neocore.host.chat;

public interface ChattablePlayer {
	
	public void setDisplayName(String name);
	public String getDisplayName();
	
	public void sendMessage(String message);
	
}
