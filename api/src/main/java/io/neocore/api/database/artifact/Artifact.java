package io.neocore.api.database.artifact;

import java.util.Date;
import java.util.UUID;

import io.neocore.api.database.Persistent;

public interface Artifact extends Persistent {

	/**
	 * Returns a UUID based on the creation time of the artifact.
	 * 
	 * @return The immutable unique identifier of the artifact.
	 */
	public UUID getUniqueId();

	/**
	 * @return The date that this artifact was created.
	 */
	public Date getCreationDate();

	/**
	 * @return The date that this artifact's data was last updated.
	 */
	public Date getUpdateDate();

	/**
	 * Returns the immutable artifact of the artifact. This cannot change as it
	 * is used to differentiate how to use the artifact.
	 * 
	 * @return The type of the artifact.
	 */
	public String getType();

	/**
	 * Sets the "owner" ID of this artifact. This can be used when trying to
	 * represent things bound onto players or other artifacts. Can be null if it
	 * is not relevant to the
	 * 
	 * @param uuid
	 */
	public void setOwnerId(UUID uuid);

	/**
	 * @see setOwnerId(UUID)
	 * 
	 * @return The ID of the owner of this artifact.
	 */
	public UUID getOwnerId();

	/**
	 * Sets the data components of this artifact.
	 * 
	 * @param data
	 *            The data to set.
	 */
	public void setData(String data);

	/**
	 * @return The data on this artifact.
	 */
	public String getData();

}
