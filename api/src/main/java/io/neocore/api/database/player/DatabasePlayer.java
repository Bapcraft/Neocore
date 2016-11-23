package io.neocore.api.database.player;

import java.util.Date;
import java.util.List;

import io.neocore.api.database.PersistentPlayerIdentity;
import io.neocore.api.eco.Account;
import io.neocore.api.player.PlayerIdentity;
import io.neocore.api.player.extension.Extension;
import io.neocore.api.player.extension.ExtensionType;
import io.neocore.api.player.group.Group;
import io.neocore.api.player.group.GroupMembership;

public interface DatabasePlayer extends PlayerIdentity, PersistentPlayerIdentity {
	
	/**
	 * @return A list of all the groups this player has.
	 */
	public List<GroupMembership> getGroupMemberships();
	
	/**
	 * Adds the group membership specified to the player in the database, and
	 * commits the change.
	 * 
	 * @param group The group membership to be added.
	 */
	public void addGroupMembership(GroupMembership gm);
	
	/**
	 * Creates a new group membership for the given group.
	 * 
	 * @param group The group to create a membership for.
	 * @return The created group membership.
	 */
	public GroupMembership addGroup(Group group);
	
	/**
	 * Removes the group membership specified from the player in the database,
	 * and commits the change.
	 * 
	 * @param group The group membership to be removed.
	 */
	public void removeGroupMembership(GroupMembership gm);
	
	/**
	 * Gets the player's global account for the given currency.  Creates a new
	 * account if none exists.
	 * 
	 * @param currency The name of the currency.
	 * @return The account object representing their store of the currency.
	 */
	public Account getAccount(String currency);
	
	/**
	 * Gets all of the accounts that this player has currently.
	 * 
	 * @return A list of the player's accounts.
	 */
	public List<Account> getAccounts();
	
	/**
	 * Attaches the extension onto the player, replacing any other extensions
	 * of that type.
	 * 
	 * @param ext The extension to attach.
	 */
	public void putExtension(Extension ext);
	
	/**
	 * Gets the extension of the given name/type.
	 * 
	 * @param name The name of the extension type.
	 * @return The extension we have on the player, or <code>null</code> if we don't have it.
	 */
	public Extension getExtension(String name);
	
	/**
	 * Gets the identifier names of all of the extensions this player has.
	 * 
	 * @return The list of names.
	 */
	public List<String> getExtensionNames();
	
	/**
	 * Gets the extension by class.  Returns null if not present on the player.
	 * 
	 * @param type The type of the extension.
	 * @return The extension data.
	 */
	public default Extension getExtension(Class<? extends Extension> type) {
		
		ExtensionType etAnno = type.getAnnotation(ExtensionType.class);
		return etAnno != null ? this.getExtension(etAnno.name()) : null;
		
	}
	
	/**
	 * Gets all of the extensions that this player has as the actual objects.
	 * 
	 * @return The list of extensions.
	 */
	public List<Extension> getExtensions();
	
	/**
	 * Sets the record of the player's last known username.
	 * 
	 * @param name The username to set.
	 */
	public void setLastUsername(String name);
	
	/**
	 * Gets the record of the player's last known username.
	 * 
	 * @return The username.
	 */
	public String getLastUsername();
	
	/**
	 * Sets when this player apparently first logged in.
	 * 
	 * @param date When the player should have first logged in.
	 */
	public void setFirstLogin(Date date);
	
	/**
	 * Returns the time that the player first joined the server.  Typically
	 * just when this object was first constructed.
	 * 
	 * @return The time the player first joined the server.
	 */
	public Date getFirstLogin();
	
	/**
	 * Sets the last login date for this player.
	 * 
	 * @param date When the player last logged in.
	 */
	public void setLastLogin(Date date);
	
	/**
	 * @return The time that this player last logged in.
	 */
	public Date getLastLogin();
	
	/**
	 * @param count Sets the number of logins this player has done.
	 */
	public void setLoginCount(int count);
	
	/**
	 * @return Gets the number of logins this player has done.
	 */
	public int getLoginCount();
	
}
