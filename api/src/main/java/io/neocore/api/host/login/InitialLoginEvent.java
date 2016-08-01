package io.neocore.api.host.login;

import java.net.InetAddress;
import java.util.UUID;

import io.neocore.api.event.Event;

/**
 * Very tiny, lightweight class for events when players first connect.
 * 
 * @author treyzania
 */
public interface InitialLoginEvent extends Event {
	
	public UUID getPlayerUniqueId();
	public InetAddress getAddress();
	
	public void allow();
	public void disallow(String message);
	
	public boolean isPermitted();
	
}
