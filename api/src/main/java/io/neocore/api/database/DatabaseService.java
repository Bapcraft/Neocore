package io.neocore.api.database;

import io.neocore.api.ServiceProvider;
import io.neocore.api.ServiceType;
import io.neocore.api.database.artifact.ArtifactService;
import io.neocore.api.database.ban.BanService;
import io.neocore.api.database.group.GroupService;
import io.neocore.api.database.player.PlayerService;
import io.neocore.api.database.session.SessionService;

/**
 * Enumerates which kinds of services that database controllers can provide.
 * 
 * @author treyzania
 */
public enum DatabaseService implements ServiceType {

	BAN(BanService.class), // Exactly what you think it is.
	SESSION(SessionService.class), // Information about who is connecting, as
									// well as when, from where, and to do what
									// they are.
	PLAYER(PlayerService.class), // Core player data, extensions, etc.
	GROUP(GroupService.class), // Group definitions, flair, inheritance, tracks,
								// etc.
	ARTIFACT(ArtifactService.class), // Warnings, evidence, etc.
	ACTIONRECORD(null);

	private Class<? extends DatabaseServiceProvider> serviceClass;

	private DatabaseService(Class<? extends DatabaseServiceProvider> clazz) {
		this.serviceClass = clazz;
	}

	public boolean isCompatible(Class<? extends DatabaseServiceProvider> serv) {

		if (serv == null)
			return false;
		if (this.getServiceClass() == null)
			return false;

		return this.getServiceClass().isAssignableFrom(serv.getClass());

	}

	@Override
	public String getName() {
		return this.name();
	}

	@Override
	public Class<? extends ServiceProvider> getServiceClass() {
		return this.serviceClass;
	}

}
