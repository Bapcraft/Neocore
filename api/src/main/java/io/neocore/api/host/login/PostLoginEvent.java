package io.neocore.api.host.login;

import io.neocore.api.event.PlayerEvent;

public interface PostLoginEvent extends PlayerEvent {
	
	public void setJoinMessage(String message);
	public String getJoinMessage();
	
}
