package io.neocore.jdbc.session;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.database.AbstractPersistentRecord;
import io.neocore.api.database.session.EndpointMove;
import io.neocore.api.database.session.ProxiedSession;
import io.neocore.api.database.session.Session;
import io.neocore.api.database.session.SessionState;

@DatabaseTable(tableName = "sessions")
public class JdbcSession extends AbstractPersistentRecord implements ProxiedSession, Comparable<Session> {
	
	@DatabaseField(canBeNull = false, id = true)
	private UUID sessionId = UUID.randomUUID();
	
	@DatabaseField(canBeNull = false)
	private UUID uuid = UUID.randomUUID(); // This gets overridden quickly, usually.
	
	@DatabaseField(canBeNull = false)
	private String loginUsername = "";
	
	@DatabaseField(canBeNull = false)
	private String addressStr = "0.0.0.0";
	
	@DatabaseField(canBeNull = false)
	private String hostString = "undefined.localhost";
	
	@DatabaseField(canBeNull = false)
	private SessionState state = SessionState.UNKNOWN;
	
	@DatabaseField(canBeNull = false)
	private String frontend = "[undefined]";
	
	@DatabaseField
	private boolean networked = false;
	
	@ForeignCollectionField
	protected ForeignCollection<JdbcNetworkMove> moves;
	protected Set<JdbcNetworkMove> updatedMoves = new HashSet<>();
	
	@DatabaseField(canBeNull = false)
	private Date start = new Date();
	
	@DatabaseField(canBeNull = false)
	private Date end = new Date();
	
	public JdbcSession() {
		// ORMLite.
	}
	
	public JdbcSession(UUID playerId) {
		this.uuid = playerId;
	}

	@Override
	public UUID getUniqueId() {
		return this.uuid;
	}
	
	public UUID getSessionId() {
		return this.sessionId;
	}
	
	@Override
	public void setLoginUsername(String name) {
		
		this.loginUsername = name;
		this.dirty();
		
	}
	
	@Override
	public String getLoginUsername() {
		return this.loginUsername;
	}

	@Override
	public void setAddress(InetAddress addr) {
		
		this.addressStr = addr.getHostAddress();
		this.dirty();
		
	}
	
	@Override
	public InetAddress getAddress() {
		
		try {
			return InetAddress.getByName(this.addressStr);
		} catch (UnknownHostException e) {
			return null; // TODO Make this exclaim something somewhere.
		}
		
	}

	@Override
	public void setHostString(String string) {
		
		this.hostString = string;
		this.dirty();
		
	}
	
	@Override
	public String getHostString() {
		return this.hostString;
	}
	
	@Override
	public void setState(SessionState state) {
		
		this.state = state;
		this.dirty();
		
	}
	
	@Override
	public SessionState getState() {
		return this.state;
	}
	
	@Override
	public void setFrontend(String frontend) {
		
		this.frontend = frontend;
		this.dirty();
		
	}
	
	@Override
	public String getFrontend() {
		return this.frontend;
	}
	
	@Override
	public void setNetworked(boolean nw) {
		
		this.networked = nw;
		this.dirty();
		
	}
	
	@Override
	public boolean isNetworked() {
		return this.networked;
	}
	
	@Override
	public ProxiedSession getAsProxiedSession() {
		return this;
	}
	
	@Override
	public void setStartDate(Date start) {
		
		this.start = start;
		this.dirty();
		
	}
	
	@Override
	public Date getStartDate() {
		return this.start;
	}
	
	@Override
	public void setEndDate(Date end) {
		
		this.end = end;
		this.dirty();
		
	}
	
	@Override
	public Date getEndDate() {
		return this.end;
	}
	
	@Override
	public List<EndpointMove> getEndpointMoves() {
		
		List<EndpointMove> ret = new ArrayList<>(this.moves.size());
		this.moves.forEach(r -> ret.add(r));
		return ret;
		
	}

	@Override
	public EndpointMove createEndpointMove() {
		
		JdbcNetworkMove nm = new JdbcNetworkMove(this);
		
		if (this.moves != null) {
			
			this.moves.add(nm);
			this.updatedMoves.add(nm);
			this.dirty();
			
		}
		
		return nm;
		
	}

	@Override
	public int compareTo(Session o) {
		return this.getUniqueId().compareTo(o.getUniqueId());
	}
	
	protected void cleanupCachedSaves() {
		
		if (this.moves != null) {
			
			this.updatedMoves.forEach(m -> {
				
				try {
					this.moves.update(m);
				} catch (SQLException e) {
					NeocoreAPI.getLogger().log(Level.SEVERE, "Problem flushing move data.", e);
				}
				
			});
			
		} else {
			NeocoreAPI.getLogger().warning("Missing moves collection for " + this.uuid + "!  Was it flushed to the database?");
		}
		
		this.updatedMoves.clear();
		
	}
	
}
