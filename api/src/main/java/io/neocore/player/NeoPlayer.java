package io.neocore.player;

import java.util.UUID;

import io.neocore.ServiceType;
import io.neocore.database.DatabaseService;
import io.neocore.database.player.DatabasePlayer;
import io.neocore.host.HostService;
import io.neocore.host.chat.ChattablePlayer;
import io.neocore.host.login.ServerPlayer;
import io.neocore.host.permissions.PermissedPlayer;
import io.neocore.host.proxy.ProxiedPlayer;
import io.neocore.player.group.Group;
import io.neocore.player.group.GroupMembership;

public class NeoPlayer implements PlayerIdentity {
	
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
	@Override
	public UUID getUniqueId() {
		return this.uuid;
	}
	
	/**
	 * @return The username of the player.
	 */
	public String getUsername() {
		
		verify(this.playerPersona, HostService.LOGIN);
		return this.playerPersona.getName();
		
	}
	
	/**
	 * @return The name of the player that should actually be displayed in chat and such.
	 */
	public String getDisplayName() {
		
		verify(this.playerChat, HostService.CHAT);
		return this.playerChat.getDisplayName();
		
	}
	
	/**
	 * Checks to see if the player is explicitly stated to be a member of the group.
	 * 
	 * @param group The group to be verified.
	 * @return
	 */
	public boolean hasGroup(Group group) {
		
		verify(this.playerRecord, DatabaseService.PLAYER);
		
		for (GroupMembership g : this.playerRecord.getGroupMemberships()) {
			if (g.getGroup().getName().equals(group.getName())) return true;
		}
		
		return false;
		
	}
	
	/**
	 * Sends a message to the player.
	 * 
	 * @param message The message to be sent
	 */
	public void sendMessage(String message) {
		
		verify(this.playerChat, HostService.CHAT);
		this.playerChat.sendMessage(message);
		
	}
	
	/**
	 * Checks to see if the player has the specified permission node.
	 * 
	 * @param node The node in question
	 * @return If the player has the permission node.
	 */
	public boolean hasPermission(String node) {
		
		verify(this.playerPermissions, HostService.PERMISSIONS);
		return this.playerPermissions.hasPermission(node);
		
	}
	
	private static void verify(PlayerIdentity pi, ServiceType serv) {
		if (pi == null) throw new UnsupportedServiceException(serv);
	}
	
}
