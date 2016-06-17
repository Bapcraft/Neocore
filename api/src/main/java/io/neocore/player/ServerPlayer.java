package io.neocore.player;

import java.util.UUID;

/**
 * Represents the object provided by the underlying server software to represent the player in the server.
 * 
 * @author treyzania
 */
public interface ServerPlayer {
	
	public UUID getUniqueId();
	
	public String getName();
	public String getDisplayName();
	
	public void sendMessage(String string);
	public boolean isOp();
	
	public boolean hasPermission(String perm);
	
}
