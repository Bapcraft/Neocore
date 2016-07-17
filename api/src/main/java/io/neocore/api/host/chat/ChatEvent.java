package io.neocore.api.host.chat;

import io.neocore.api.event.Cancellable;
import io.neocore.api.event.PlayerEvent;

public interface ChatEvent extends PlayerEvent, Cancellable {
	
	public void setMessage(String message);
	public String getMessage();
	
}
