package io.neocore.api.database.session;

import java.net.InetAddress;
import java.util.Date;
import java.util.UUID;

import io.neocore.api.database.RemoveInvalidatable;
import io.neocore.api.player.PlayerIdentity;

/**
 * A session object representing the player's time on the server from an
 * initial connection to their disconnection.  Must be kept closely in-sync
 * with the database.
 * 
 * @author treyzania
 */
public class Session implements PlayerIdentity, RemoveInvalidatable {
	
	private SessionService service;
	
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
	
	private transient boolean dirty, valid;
	
	public Session(SessionService serv, UUID uuid, String name, InetAddress src, String inbound) {
		
		this.service = serv;
		
		this.uuid = uuid;
		this.username = name;
		this.address = src;
		
		this.inboundServer = inbound;
		
		this.valid = true;
		
	}
	
	@Override
	public UUID getUniqueId() {
		return this.getUniqueId();
	}
	
	/**
	 * @return <code>true</code> if the session is still ongoing, <code>false</code> otherwise.
	 */
	public boolean isOver() {
		return this.getEndDate() != null && this.getEndDate() != new Date(-1L);
	}
	
	/**
	 * Sets the state and dirties the session.
	 * 
	 * @param s
	 */
	public void setState(SessionState s) {
		
		this.state = s;
		this.dirty();
		
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
		this.dirty();
		
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
		this.dirty();
		
	}
	
	/**
	 * @return The end date.
	 */
	public Date getEndDate() {
		return this.end;
	}
	
	@Override
	public void setDirty(boolean val) {
		this.dirty = val;
	}

	@Override
	public boolean isDirty() {
		return this.dirty;
	}
	
	@Override
	public void dirty() {
		
		if (!this.isGloballyValid()) throw new IllegalStateException("Cannot mark an invalid object as dirty!");
		
		this.setDirty(true);
		this.service.flush(this, () -> {}); // The service should mark it as clean.
		
	}

	@Override
	public boolean isGloballyValid() {
		return this.valid;
	}

	@Override
	public void invalidate() {
		
		this.valid = false;
		// TODO Automatically queue up the reload.
		
	}
	
}
