package io.neocore.jdbc.session;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import io.neocore.api.database.AbstractPersistentRecord;
import io.neocore.api.database.session.EndpointMove;
import io.neocore.api.infrastructure.NetworkEndpoint;

@DatabaseTable(tableName = "networkMoves")
public class JdbcNetworkMove extends AbstractPersistentRecord implements EndpointMove {
	
	@DatabaseField(foreign = true)
	private JdbcSession session;
	
	@DatabaseField(canBeNull = false)
	private Date time = new Date();
	
	@DatabaseField(canBeNull = false)
	private String destination = "[undefined]";
	
	protected JdbcNetworkMove() {
		
	}
	
	public JdbcNetworkMove(JdbcSession session) {
		this.session = session;
	}
	
	@Override
	public void dirty() {
		
		super.dirty();
		this.session.dirty();
		
	}

	@Override
	public boolean isDirty() {
		return super.isDirty() || this.session.isDirty();
	}
	
	@Override
	public boolean isGloballyValid() {
		return super.isGloballyValid() || this.session.isGloballyValid();
	}
	
	public String getNetwork() {
		
		// Should always evaluate to the first one, but I'm leaving it like this in case.
		return this.session.isNetworked() ? this.session.getFrontend() : null;
		
	}
	
	@Override
	public void setTime(Date time) {
		this.time = time;
	}
	
	@Override
	public Date getTime() {
		return this.time;
	}
	
	@Override
	public void setDestination(NetworkEndpoint ep) {
		this.destination = ep.getNetworkName();
	}
	
	@Override
	public NetworkEndpoint getDestination() {
		return null; // FIXME Make this look for actual network support.
	}
	
}
