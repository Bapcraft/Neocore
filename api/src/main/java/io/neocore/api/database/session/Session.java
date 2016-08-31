package io.neocore.api.database.session;

import java.net.InetAddress;
import java.util.Date;
import java.util.UUID;

import io.neocore.api.host.HostContext;
import io.neocore.api.player.PlayerIdentity;

/**
 * A session object representing the player's time on the server from an
 * initial connection to their disconnection.  Must be kept closely in-sync
 * with the database.
 * 
 * @author treyzania
 */
public abstract class Session implements PlayerIdentity {
	
	/** The UUID of the player whose session this is. */
	public final UUID uuid;
	
	/** The player's username when they connected to the server. */
	public final String username;
	
	/** The address that the player conneced from */
	public final InetAddress address;
	
	/** The context that the player was connecting to when they connected.  Not that of their first server */
	public final HostContext inboundContext;
	
	protected SessionState state;
	protected Date start, end;
	
	public Session(UUID uuid, String name, InetAddress src, HostContext context) {
		
		this.uuid = uuid;
		this.username = name;
		this.address = src;
		
		this.inboundContext = context;
		
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
	 * @return The current state of the session.
	 */
	public SessionState getState() {
		return this.state;
	}
	
}
