package io.neocore.api.database.session;

import java.net.InetAddress;
import java.util.Date;
import java.util.UUID;

import io.neocore.api.player.PlayerIdentity;

/**
 * A session object representing the player's time on the server from an
 * initial connection to their disconnection.  Must be kept closely in-sync
 * with the database.
 * 
 * @author treyzania
 */
public class Session implements PlayerIdentity {
	
	/** The UUID of the player whose session this is. */
	public final UUID uuid;
	
	/** The player's username when they connected to the server. */
	public final String username;
	
	/** The address that the player conneced from */
	public final InetAddress address;
	
	/** The server's name that the player was "connecting" to when they connected.  Not that of their first game server */
	public final String inboundServer;
	
	private SessionState state;
	private Date start, end;
	
	private transient boolean dirty;
	
	public Session(UUID uuid, String name, InetAddress src, String inbound) {
		
		this.uuid = uuid;
		this.username = name;
		this.address = src;
		
		this.inboundServer = inbound;
		
	}
	
	@Override
	public UUID getUniqueId() {
		return this.getUniqueId();
	}
	
	/**
	 * @return The time that the session started.
	 */
	public Date getStart() {
		return this.start;
	}
	
	/**
	 * @return The time that the session ended, be it due to kick, timeout, or manual disconnect.
	 */
	public Date getEnd() {
		return this.start;
	}
	
	/**
	 * @return <code>true</code> if the session is still ongoing, <code>false</code> otherwise.
	 */
	public boolean isOver() {
		return this.getEnd() != null;
	}
	
	/**
	 * Sets the state and dirties the session.
	 * 
	 * @param s
	 */
	public void setState(SessionState s) {
		
		this.state = s;
		this.dirty = true;
		
	}
	
	/**
	 * @return The current state of the session.
	 */
	public SessionState getState() {
		return this.state;
	}
	
	/**
	 * @return <code>true</code> if this session is throughout a network and has transfers, <code>false<code> otherwise.
	 */
	public boolean isNetwork() {
		return false;
	}
	
	/**
	 * Sets the start date and dirties the session.
	 * 
	 * @param d The start date.
	 */
	public void setStartDate(Date d) {
		
		this.start = d;
		this.dirty = true;
		
	}
	
	/**
	 * @return The start date.
	 */
	public Date getStartDate() {
		return this.start;
	}
	
	/**
	 * Sets the end date and dirties the session.
	 * 
	 * @param d The end date.
	 */
	public void setEndDate(Date d) {
		
		this.end = d;
		this.dirty = true;
		
	}
	
	/**
	 * @return The end date.
	 */
	public Date getEndDate() {
		return this.end;
	}
	
	/**
	 * Cleans this filty whore.
	 */
	public void clean() {
		this.dirty = false;
	}
	
	/**
	 * @return The dirtiness of this Session object.
	 */
	public boolean isDirty() {
		return this.dirty;
	}
	
}
