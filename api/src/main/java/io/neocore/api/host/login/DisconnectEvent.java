package io.neocore.api.host.login;

import io.neocore.api.event.PlayerEvent;

public interface DisconnectEvent extends PlayerEvent {
	
	public void setQuitMessage(String message);
	public String getQuitMessage();
	
}
