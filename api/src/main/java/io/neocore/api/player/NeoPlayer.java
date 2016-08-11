package io.neocore.api.player;

import java.util.UUID;

import io.neocore.api.ServiceType;
import io.neocore.api.database.DatabaseService;
import io.neocore.api.database.player.DatabasePlayer;
import io.neocore.api.host.HostService;
import io.neocore.api.host.chat.ChattablePlayer;
import io.neocore.api.host.login.ServerPlayer;
import io.neocore.api.host.permissions.PermissedPlayer;
import io.neocore.api.host.proxy.NetworkPlayer;
import io.neocore.api.player.group.Group;
import io.neocore.api.player.group.GroupMembership;

public class NeoPlayer implements PlayerIdentity, Comparable<NeoPlayer> {
	
	private UUID uuid;
	
	// Host service identities
	protected ServerPlayer playerPersona;
	protected NetworkPlayer playerNetworkEntity;
	protected PermissedPlayer playerPermissions;
	protected ChattablePlayer playerChat;
	
	// Database service identities
	protected DatabasePlayer playerRecord;
	
	public NeoPlayer(UUID uuid) {
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

	@Override
	public int compareTo(NeoPlayer o) {
		return this.uuid.compareTo(o.uuid);
	}
	
}
