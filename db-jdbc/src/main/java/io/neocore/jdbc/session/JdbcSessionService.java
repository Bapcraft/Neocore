package io.neocore.jdbc.session;

import java.util.UUID;
import com.j256.ormlite.dao.Dao;

import io.neocore.api.database.session.Session;
import io.neocore.api.database.session.SessionService;

public class JdbcSessionService implements SessionService {
	
	private Dao<JdbcSession, UUID> sessionDao;
	
	@Override
	public void flush(UUID uuid) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void invalidate(UUID uuid) {
		// TODO Auto-generated method stub

	}

	@Override
	public Session getSession(UUID uuid) {
		return null;
	}
	
}
