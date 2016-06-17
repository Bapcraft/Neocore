package io.neocore.host.chat;

import io.neocore.event.Cancellable;
import io.neocore.event.PlayerEvent;

public interface ChatEvent extends PlayerEvent, Cancellable {
	
	public void setMessage(String message);
	public String getMessage();
	
}
