package io.neocore.api.database.session;

import java.net.InetAddress;
import java.util.Date;
import java.util.UUID;

import io.neocore.api.host.HostContext;
import io.neocore.api.player.PlayerIdentity;

public abstract class Session implements PlayerIdentity {
	
	public final UUID uuid;
	public final String username;
	public final InetAddress address;
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

	public Date getStart() {
		return this.start;
	}
	
	public Date getEnd() {
		return this.start;
	}
	
	public boolean isOver() {
		return this.getEnd() != null;
	}
	
	public SessionState getState() {
		return this.state;
	}
	
}
