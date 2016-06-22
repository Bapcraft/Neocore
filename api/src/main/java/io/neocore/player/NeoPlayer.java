package io.neocore.player;

import java.util.UUID;

import io.neocore.database.player.DatabasePlayer;
import io.neocore.host.chat.ChattablePlayer;
import io.neocore.host.login.ServerPlayer;
import io.neocore.host.permissions.PermissedPlayer;
import io.neocore.host.proxy.ProxiedPlayer;
import io.neocore.player.extension.PlayerExtension;
import io.neocore.player.group.Group;
import io.neocore.player.group.GroupMembership;

public class NeoPlayer {
	
	private UUID uuid;
	
	// Host service providers
	protected ServerPlayer playerPersona;
	protected ProxiedPlayer playerProxyAgent;
	protected PermissedPlayer playerPermissions;
	protected ChattablePlayer playerChat;
	
	// Database service provider
	protected DatabasePlayer playerRecord;
	
	protected NeoPlayer(UUID uuid) {
		this.uuid = uuid;
	}
	
	/**
	 * @return The UUID of the player.
	 */
	public UUID getUniqueId() {
		return this.uuid;
	}
	
	/**
	 * @return The username of the player.
	 */
	public String getUsername() {
		return this.playerPersona.getName();
	}
	
	/**
	 * @return The name of the player that should actually be displayed in chat and such.
	 */
	public String getDisplayName() {
		return this.playerChat.getDisplayName();
	}
	
	/**
	 * Find the PlayerExtension specified by the class given. 
	 * 
	 * @param name
	 * @return
	 */
	public PlayerExtension getExtension(Class<? extends PlayerExtension> clazz) {
		
		PlayerExtension[] exts = this.playerRecord.getExtensions();
		
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
		
		for (GroupMembership g : this.playerRecord.getGroupMemberships()) {
			
			Group currentNode = g.getGroup();
			
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
		
		for (GroupMembership g : this.playerRecord.getGroupMemberships()) {
			if (g.getGroup().getName().equals(group.getName())) return true;
		}
		
		return false;
		
	}
	
}
