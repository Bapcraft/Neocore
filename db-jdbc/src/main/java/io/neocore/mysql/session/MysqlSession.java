package io.neocore.mysql.session;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import io.neocore.api.database.AbstractPersistentRecord;
import io.neocore.api.database.session.EndpointMove;
import io.neocore.api.database.session.ProxiedSession;
import io.neocore.api.database.session.SessionState;

@DatabaseTable(tableName = "sessions")
public class MysqlSession extends AbstractPersistentRecord implements ProxiedSession {
	
	@DatabaseField(canBeNull = false, id = true)
	private UUID sessionId;
	
	@DatabaseField(canBeNull = false)
	private UUID uuid;
	
	@DatabaseField(canBeNull = false)
	private String loginUsername;
	
	@DatabaseField(canBeNull = false)
	private String addressStr;
	
	@DatabaseField(canBeNull = false)
	private String hostString;
	
	@DatabaseField(canBeNull = false)
	private SessionState state;
	
	@DatabaseField(canBeNull = false)
	private String frontend;
	
	@DatabaseField
	private boolean networked;
	
	@ForeignCollectionField
	private ForeignCollection<MysqlNetworkMove> moves;
	
	@DatabaseField(canBeNull = false)
	private Date start;
	
	@DatabaseField(canBeNull = false)
	private Date end;
	
	public MysqlSession() {
		
		// ORMLite.
		
		// So we don't do something totally crazy.
		this.sessionId = UUID.randomUUID();
		
	}
	
	public MysqlSession(UUID sessionId, UUID playerId, String playerName, InetAddress address) {
		
		this.sessionId = sessionId;
		this.uuid = playerId;
		this.loginUsername = playerName;
		this.addressStr = address.getHostAddress();
		this.hostString = address.toString();
		
	}
	
	@Override
	public String getLoginUsername() {
		return this.loginUsername;
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
	public UUID getUniqueId() {
		return this.uuid;
	}

	@Override
	public List<EndpointMove> getEndpointMoves() {
		
		List<EndpointMove> ret = new ArrayList<>(this.moves.size());
		this.moves.forEach(r -> ret.add(r));
		return ret;
		
	}

	@Override
	public void addEndpointMove(EndpointMove move) {
		
		this.moves.add((MysqlNetworkMove) move); // FIXME Casting.
		this.dirty();
		
	}
	
}
