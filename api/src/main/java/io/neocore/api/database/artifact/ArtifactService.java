package io.neocore.api.database.artifact;

import java.util.List;
import java.util.UUID;

import io.neocore.api.database.DatabaseServiceProvider;

public interface ArtifactService extends DatabaseServiceProvider {

	/**
	 * Creates a new artifact with the given parameters and stores it in the
	 * database. Returns <code>null</code> in the event of failure.
	 * 
	 * @param owner
	 *            The "owner" UUID of the artifact.
	 * @param type
	 *            The type of the artifact.
	 * @param data
	 *            The data the artifact stores.
	 * @return The artifact.
	 */
	public Artifact createArtifact(UUID owner, String type, String data);

	/**
	 * Creates a new artifact with the given parameters, assuming
	 * <code>null</code> for the "owner" value, and stores it in the database.
	 * Returns <code>null</code> in the event of failure.
	 * 
	 * @param type
	 *            The type of the artifact
	 * @param data
	 *            The data the artifact stores
	 * @return The artifact
	 */
	public default Artifact createArtifact(String type, String data) {
		return this.createArtifact(null, type, data);
	}

	/**
	 * Gets the artifact with the given unique ID.
	 * 
	 * @param uuid
	 *            The UUID of the artifact
	 * @return The artifact, or <code>null</code> if not found
	 */
	public Artifact getArtifact(UUID uuid);

	/**
	 * Returns all artifacts of the specified type.
	 * 
	 * @param type
	 *            The type of the artifact
	 * @return A list of artifacts
	 */
	public List<Artifact> getArtifacts(String type);

	/**
	 * Gets the singleton artifact with the given type/key.
	 * 
	 * @param key
	 *            The type/key of the artifact
	 * @return The singleton artifact
	 */
	public default Artifact getSingletonArtifact(String key) {
		return this.getArtifacts(key).get(0);
	}

	/**
	 * Returns all artifacts with the specified owner ID. Passing
	 * <code>null</code> will return artifacts without owners, as expected.
	 * 
	 * @param ownerId
	 *            The UUID of the "owner"
	 * @return A list of artifacts
	 */
	public List<Artifact> getArtifacts(UUID ownerId);

}
