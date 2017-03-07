package io.neocore.jdbc.ban;

import java.util.Date;
import java.util.UUID;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import io.neocore.api.database.AbstractPersistentRecord;
import io.neocore.api.database.ban.BanEntry;
import io.neocore.api.host.Context;

@DatabaseTable(tableName = "bans")
public class JdbcBan extends AbstractPersistentRecord implements BanEntry {

	@DatabaseField(id = true)
	private UUID banId;

	@DatabaseField(canBeNull = false)
	private UUID playerId = new UUID(0, 0);

	@DatabaseField(canBeNull = false)
	private UUID issuerId = new UUID(0, 0);

	@DatabaseField
	private Date issue;

	@DatabaseField
	private Date start;

	@DatabaseField
	private Date end;

	@DatabaseField(canBeNull = false)
	private String reason = "[undefined]";

	@DatabaseField
	private String context;

	public JdbcBan() {
		this.banId = UUID.randomUUID();
	}

	public JdbcBan(UUID uuid) {

		this();

		this.playerId = uuid;

	}

	public UUID getBanId() {
		return this.banId;
	}

	@Override
	public void setPlayerId(UUID playerId) {

		this.playerId = playerId;
		this.dirty();

	}

	@Override
	public UUID getPlayerId() {
		return this.playerId;
	}

	@Override
	public void setDateIssued(Date date) {

		this.issue = date;
		this.dirty();

	}

	@Override
	public Date getDateIssued() {
		return this.issue;
	}

	@Override
	public void setStartDate(Date date) {

		this.start = date;
		this.dirty();

	}

	@Override
	public Date getStartDate() {
		return this.start;
	}

	@Override
	public void setExpirationDate(Date date) {

		this.end = date;
		this.dirty();

	}

	@Override
	public Date getExpirationDate() {
		return this.end;
	}

	@Override
	public void setIssuerId(UUID issuerId) {

		this.issuerId = issuerId;
		this.dirty();

	}

	@Override
	public UUID getIssuerId() {
		return this.issuerId;
	}

	@Override
	public void setReason(String reason) {

		this.reason = reason;
		this.dirty();

	}

	@Override
	public String getReason() {
		return this.reason;
	}

	@Override
	public void setContext(Context context) {

		this.context = context.getName();
		this.dirty();

	}

	@Override
	public Context getContext() {
		return Context.create(this.context);
	}

}
