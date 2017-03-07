package io.neocore.api.database.artifact;

import java.io.IOException;
import java.util.UUID;

import io.neocore.api.database.player.PlayerService;

public class IdentifierManager {

	private ArtifactService artifacts;
	private PlayerService players;

	public IdentifierManager() {

	}

	public void setArtifactService(ArtifactService serv) {
		this.artifacts = serv;
	}

	public void setPlayerService(PlayerService serv) {
		this.players = serv;
	}

	private Artifact getArtifact(String name) {

		if (this.artifacts == null)
			throw new UnsupportedOperationException("No artifact service loaded.");
		return this.artifacts.getArtifacts(ArtifactTypes.DATA_IDENTIFIER_PREFIX + "." + name).get(0);

	}

	public String getLiteralIdentifierValue(String name) {
		return this.getArtifact(name).getData();
	}

	public UUID getLiteralIdentifierId(String name) {
		return this.getArtifact(name).getUniqueId();
	}

	public void setLiteralIdentiferValue(String name, String value) {

		if (this.artifacts == null)
			throw new UnsupportedOperationException("No artifact service loaded.");

		Artifact art = this.getArtifact(name);
		if (art != null) {

			art.setData(value);
			art.flush();

		} else {
			this.artifacts.createArtifact(name, value);
		}

	}

	public String getIdentifier(UUID uuid) {

		if (uuid.version() == 3) {

			if (this.artifacts == null)
				throw new UnsupportedOperationException(
						"No artifact service loaded!  Version 3 UUIDs can't be resolved.");
			return this.artifacts.getArtifact(uuid).getData();

		} else if (uuid.version() == 4) {

			if (this.players == null)
				throw new UnsupportedOperationException(
						"No player service loaded!  Version 4 UUIDs can't be resolved.");
			try {
				return this.players.getLastUsername(uuid);
			} catch (IOException e) {
				return "[unknown]";
			}

		}

		return "[invalid_identifier]";

	}

}
