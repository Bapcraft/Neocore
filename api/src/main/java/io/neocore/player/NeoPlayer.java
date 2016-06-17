package io.neocore.player;

import io.neocore.player.extension.PlayerExtension;
import io.neocore.player.group.Group;

public class NeoPlayer {
	
	protected ServerPlayer serverPlayer;
	protected DatabasePlayer databasePlayer;
	
	protected NeoPlayer(ServerPlayer sp, DatabasePlayer dbp) {
		
		this.serverPlayer = sp;
		this.databasePlayer = dbp;
		
	}
	
	/**
	 * @return The username of the player.
	 */
	public String getUsername() {
		return this.serverPlayer.getName();
	}
	
	/**
	 * @return The name of the player that should actually be displayed in chat and such.
	 */
	public String getDisplayName() {
		return this.serverPlayer.getDisplayName();
	}
	
	/**
	 * Find the PlayerExtension specified by the class given. 
	 * 
	 * @param name
	 * @return
	 */
	public PlayerExtension getExtension(Class<? extends PlayerExtension> clazz) {
		
		PlayerExtension[] exts = this.databasePlayer.getExtensions();
		
		for (PlayerExtension pe : exts) {
			if (clazz.isAssignableFrom(pe.getClass())) return pe;
		}
		
		return null;
		
	}
	
	/**
	 * Checks to see if the player has the group at all, weather explicitly of by inheritance.
	 * 
	 * @param group The group to be verified.
	 * @return
	 */
	public boolean hasInheritedGroup(Group group) {
		
		for (Group g : this.databasePlayer.getGroups()) {
			
			Group currentNode = g;
			
			// Traverse up the graph and find one that matches.
			while (currentNode != null) {
				
				if (currentNode.getName().equals(group.getName())) return true;
				currentNode = currentNode.getParent();
				
			}
			
		}
		
		return false;
		
	}
	
	/**
	 * Checks to see if the player is explicitly stated to be a member of the group.
	 * 
	 * @param group The group to be verified.
	 * @return
	 */
	public boolean hasGroup(Group group) {
		
		for (Group g : this.databasePlayer.getGroups()) {
			if (g.getName().equals(group.getName())) return true;
		}
		
		return false;
		
	}
	
	public ServerPlayer getHostPlayer() {
		return this.serverPlayer;
	}
	
	public DatabasePlayer getDatabasePlayer() {
		return this.databasePlayer;
	}
	
}
