package io.neocore.player;

import java.net.InetAddress;
import java.util.UUID;

/**
 * Represents the object provided by the underlying server software to represent the player in the server.
 * 
 * @author treyzania
 */
public interface ServerPlayer {
	
	public UUID getUniqueId();
	public String getName();
	public InetAddress getAddress();
	
	public String getDisplayName();
	
	public void sendMessage(String string);
	public boolean isOp();
	
	public boolean hasPermission(String perm);
	
	public void kick(String message);
	
}
