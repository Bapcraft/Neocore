package io.neocore.jdbc.artifact;

import java.util.Date;
import java.util.UUID;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import io.neocore.api.database.AbstractPersistentRecord;
import io.neocore.api.database.artifact.Artifact;
import io.neocore.api.database.artifact.ArtifactTypes;

@DatabaseTable(tableName = "artifacts")
public class JdbcArtifact extends AbstractPersistentRecord implements Artifact {

	@DatabaseField(id = true, canBeNull = false)
	private UUID uuid;

	@DatabaseField(canBeNull = false)
	private Date created;

	@DatabaseField(canBeNull = false)
	private Date updated;

	@DatabaseField(canBeNull = false)
	private String type = ArtifactTypes.UNCLASSIFIED;

	@DatabaseField
	private UUID ownerId;

	@DatabaseField
	public String data;

	public JdbcArtifact() {

		this.uuid = new UUID(0, 0);
		this.created = new Date();
		this.updated = this.created;

	}

	public JdbcArtifact(UUID owner, String type, String data) {

		this.created = new Date();
		this.updated = this.created;

		this.ownerId = owner;
		this.type = type != null ? type : ArtifactTypes.UNCLASSIFIED;
		this.data = data != null ? data : "";

		// Yucky expression to get something unique and useful.
		String hashee = this.type + '\0' + this.data + '\0' + this.created.toString()
				+ (owner != null ? Character.toString('\0') + owner : "");
		this.uuid = UUID.nameUUIDFromBytes(hashee.getBytes());

	}

	@Override
	public UUID getUniqueId() {
		return this.uuid;
	}

	@Override
	public Date getCreationDate() {
		return this.created;
	}

	@Override
	public Date getUpdateDate() {
		return this.updated;
	}

	@Override
	public String getType() {
		return this.type;
	}

	@Override
	public void setOwnerId(UUID uuid) {

		this.ownerId = uuid;
		this.dirty();

	}

	@Override
	public UUID getOwnerId() {
		return this.ownerId;
	}

	@Override
	public void setData(String data) {

		this.data = data;
		this.dirty();

	}

	@Override
	public String getData() {
		return this.data;
	}

	@Override
	public void dirty() {

		this.updated = new Date();
		super.dirty();

	}

}
