package io.neocore.api.player;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.google.common.base.Preconditions;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.database.AbstractPersistentRecord;
import io.neocore.api.database.Persistent;
import io.neocore.api.database.PersistentPlayerIdentity;
import io.neocore.api.database.player.DatabasePlayer;
import io.neocore.api.database.session.Session;
import io.neocore.api.host.chat.ChattablePlayer;
import io.neocore.api.host.login.ServerPlayer;
import io.neocore.api.host.permissions.PermissedPlayer;
import io.neocore.api.player.group.Group;
import io.neocore.api.player.group.GroupMembership;

/**
 * Object intended to encompass all of the data that a player can have tied
 * into the different capabilities of the server and any data provided by the
 * database.  Mostly just a wrapper/container class.
 * 
 * @author treyzania
 */
public class NeoPlayer extends AbstractPersistentRecord implements PersistentPlayerIdentity, Comparable<NeoPlayer> {
	
	/**
	 * The player's unique ID, according to Mojang.
	 */
	private UUID uuid;
	
	/**
	 * The time this NeoPlayer was constructed.
	 */
	private Date constructionTime;
	
	/**
	 * The player's in-game identities that we can use.
	 */
	protected List<PlayerIdentity> identities;
	
	/**
	 * Set if the player has been fully populated with its required identites.
	 */
	private boolean isPopulated;
	
	public NeoPlayer(UUID uuid) {
		
		this.uuid = uuid;
		this.identities = new ArrayList<>();
		
		this.constructionTime = new Date();
		NeocoreAPI.getLogger().info("Constructed new NeoPlayer with ID: " + uuid);
		
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
		return this.getIdentity(Session.class);
	}
	
	/**
	 * @return The username of the player.
	 */
	public String getUsername() {
		return this.getIdentity(ServerPlayer.class).getName();
	}
	
	/**
	 * @return The name of the player that should actually be displayed in chat and such.
	 */
	public String getDisplayName() {
		return this.getIdentity(ChattablePlayer.class).getDisplayName();
	}
	
	/**
	 * Checks to see if the player is explicitly stated to be a member of the group.
	 * 
	 * @param group The group to be verified.
	 * @return
	 */
	public boolean hasGroup(Group group) {
		
		try {
			
			for (GroupMembership g : this.getIdentity(DatabasePlayer.class).getGroupMemberships()) {
				if (g.getGroup().getName().equals(group.getName())) return true;
			}
			
		} catch (IllegalArgumentException e) {
			return false;
		}
		
		return false;
		
	}
	
	/**
	 * Sends a message to the player.
	 * 
	 * @param message The message to be sent
	 */
	public void sendMessage(String message) {
		this.getIdentity(ChattablePlayer.class).sendMessage(message);
	}
	
	/**
	 * Checks to see if the player has the specified permission node.
	 * 
	 * @param node The node in question
	 * @return If the player has the permission node.
	 */
	public boolean hasPermission(String node) {
		
		try {
			return this.getIdentity(PermissedPlayer.class).hasPermission(node);
		} catch (IllegalArgumentException e) {
			return false;
		}
		
	}
	
	public boolean hasIdentity(Class<? extends PlayerIdentity> clazz) {
		
		try {
			
			// If it's a null somehow, then it should return false anyways.
			return this.getIdentity(clazz) != null;
			
		} catch (IllegalArgumentException e) {
			
			// If it errors, then we definitely don't have it.
			return false;
			
		}
		
	}
	
	/**
	 * Adds the identity of provided onto the player, assuming there isn't
	 * already one of that type present.
	 * 
	 * @param ident The identity.
	 */
	public void addIdentity(PlayerIdentity ident) {
		
		Preconditions.checkNotNull(ident);
		if (this.hasIdentity(ident.getClass())) throw new IllegalArgumentException("Alredy an identity of that type!");
		
		this.identities.add(ident);
		
	}
	
	/**
	 * Marks this player as fully populated.  Calling this if you're not
	 * supposed to can cause unexpected behavior.
	 */
	public void setPopulated() {
		this.isPopulated = true;
	}
	
	/**
	 * @return If the player has been marked as fully populated.
	 */
	public boolean isPopulated() {
		return this.isPopulated;
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
		
		if (clazz == null) return null;
		
		for (PlayerIdentity ident : this.identities) {
			if (ident != null && clazz.isAssignableFrom(ident.getClass())) return (T) ident;
		}
		
		return null;
		
	}
	
	public List<PlayerIdentity> getIdentities() {
		return new ArrayList<>(this.identities);
	}
	
	/**
	 * Returns the time that the NeoPlayer was constructed.  This is
	 * effectively the time that this player last was revalidated, or the time
	 * they connected if they have not been ever revalidated.
	 * 
	 * @see ServerPlayer
	 * @see Session
	 * 
	 * @return The time this NeoPlayer was constructed.
	 */
	public Date getConstructionTime() {
		return this.constructionTime;
	}
	
	@Override
	public int compareTo(NeoPlayer o) {
		return this.uuid.compareTo(o.uuid);
	}
	
	@Override
	public boolean isDirty() {
		
		if (super.isDirty()) return true;
		
		for (PlayerIdentity pi : this.identities) {
			if (pi instanceof Persistent && ((Persistent) pi).isDirty()) return true; 
		}
		
		return false;
		
	}
	
	@Override
	public void flush() {
		
		// We don't really pay attention to the dirtyness of identities.
		this.getFlushProcedure().run();
		this.setDirty(false);
		
	}
	
}
