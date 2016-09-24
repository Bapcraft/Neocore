package io.neocore.api.player;

import java.lang.reflect.Field;
import java.util.UUID;

import io.neocore.api.ServiceType;
import io.neocore.api.UnsupportedServiceException;
import io.neocore.api.database.DatabaseService;
import io.neocore.api.database.player.DatabasePlayer;
import io.neocore.api.database.session.Session;
import io.neocore.api.host.HostService;
import io.neocore.api.host.chat.ChattablePlayer;
import io.neocore.api.host.login.ServerPlayer;
import io.neocore.api.host.permissions.PermissedPlayer;
import io.neocore.api.host.proxy.NetworkPlayer;
import io.neocore.api.player.group.Group;
import io.neocore.api.player.group.GroupMembership;

/**
 * Object intended to encompass all of the data that a player can have tied
 * into the different capabilities of the server and any data provided by the
 * database.
 * 
 * @author treyzania
 */
public class NeoPlayer implements PlayerIdentity, Comparable<NeoPlayer> {
	
	private UUID uuid;
	private Session session;
	
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
	 * @return The session of this player, or <code>null</code> if the player is not connected.
	 */
	public Session getSession() {
		return this.session;
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
	
	/**
	 * Gets the player identity thingamajig for manipulation.  This can
	 * potentially be dangerous as it bypasses wrapper methods, so use with
	 * care!
	 * 
	 * @param clazz The type to find.
	 * @return The player identity aspect thing.
	 */
	@SuppressWarnings("unchecked")
	public <T extends PlayerIdentity> T getIdentity(Class<T> clazz) {
		
		for (Field f : this.getClass().getDeclaredFields()) {
			
			if (PlayerIdentity.class.isAssignableFrom(f.getType()) && f.getType() == clazz) {
				
				try {

					boolean acc = f.isAccessible();
					f.setAccessible(true);
					Object o = f.get(this);
					f.setAccessible(acc);
					
					if (o != null) return (T) o;
					
				} catch (IllegalArgumentException | IllegalAccessException e) {
					throw new RuntimeException("You did something wrong!", e);
				}
				
			}
			
		}
		
		throw new IllegalArgumentException("This wasn't populated on the player! (" + clazz.getName() + ")");
		
	}
	
	/**
	 * Verifies that the player identity is not <code>null</code>, and if it is then  throws the relevant exception.
	 * 
	 * @param pi The object to verify.
	 * @param serv The service type to use in the exception.
	 */
	private static void verify(PlayerIdentity pi, ServiceType serv) {
		if (pi == null) throw new UnsupportedServiceException(serv);
	}
	
	@Override
	public int compareTo(NeoPlayer o) {
		return this.uuid.compareTo(o.uuid);
	}
	
}
