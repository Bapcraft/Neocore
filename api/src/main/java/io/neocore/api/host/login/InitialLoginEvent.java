package io.neocore.api.host.login;

import java.net.InetAddress;
import java.util.UUID;

/**
 * Very tiny, lightweight class for events when players first connect.
 * 
 * @author treyzania
 */
public interface InitialLoginEvent {
	
	public UUID getPlayerUniqueId();
	public InetAddress getAddress();
	
}
