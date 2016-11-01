package io.neocore.mysql.session;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import io.neocore.api.database.AbstractPersistentRecord;
import io.neocore.api.database.session.EndpointMove;
import io.neocore.api.host.proxy.NetworkEndpoint;

@DatabaseTable(tableName = "networkMoves")
public class MysqlNetworkMove extends AbstractPersistentRecord implements EndpointMove {
	
	@DatabaseField(foreign = true)
	private MysqlSession session;
	
	@DatabaseField(canBeNull = false)
	private Date time;
	
	@DatabaseField(canBeNull = false)
	private String destination;

	@Override
	public void setDirty(boolean val) {
		
		super.setDirty(val);
		this.session.setDirty(val);
		
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
		
		// Should always evaluate to the first one, but I'm leaving it like this incase.
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
		this.destination = ep.getName();
	}
	
	@Override
	public NetworkEndpoint getDestination() {
		return null; // FIXME Make this look for actual network support.
	}
	
}
