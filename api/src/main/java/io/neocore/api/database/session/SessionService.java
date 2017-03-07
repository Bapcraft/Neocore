package io.neocore.api.database.session;

import io.neocore.api.LoadAsync;
import io.neocore.api.database.DatabaseServiceProvider;
import io.neocore.api.database.IdentityLinkage;
import io.neocore.api.player.PlayerIdentity;

@LoadAsync
public interface SessionService extends DatabaseServiceProvider, IdentityLinkage<Session> {

	@Override
	default Class<? extends PlayerIdentity> getIdentityClass() {
		return Session.class;
	}

}
