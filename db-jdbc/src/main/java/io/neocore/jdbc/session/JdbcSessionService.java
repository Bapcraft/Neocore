package io.neocore.jdbc.session;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.logging.Level;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.database.DatabaseService;
import io.neocore.api.database.session.Session;
import io.neocore.api.database.session.SessionService;
import io.neocore.api.database.session.SessionState;
import io.neocore.jdbc.AbstractJdbcService;

public class JdbcSessionService extends AbstractJdbcService implements SessionService {
	
	private Set<JdbcSession> cache;
	private Dao<JdbcSession, UUID> sessionDao;
	
	private Map<UUID, UUID> playerIdToSessionIdMap;
	
	public JdbcSessionService(ConnectionSource src) {
		
		super(src);
		
		this.cache = new TreeSet<>();
		this.playerIdToSessionIdMap = new HashMap<>();
		
	}
	
	@Override
	public void init() {
		
		NeocoreAPI.getAgent().getPlayerManager().addService(DatabaseService.SESSION);
		
		try {
			
			// Create all of the DAOs we have.
			this.sessionDao = DaoManager.createDao(this.getSource(), JdbcSession.class);
			DaoManager.createDao(this.getSource(), JdbcNetworkMove.class);
			
			// Initialize tables.
			TableUtils.createTableIfNotExists(this.getSource(), JdbcSession.class);
			TableUtils.createTableIfNotExists(this.getSource(), JdbcNetworkMove.class);
			
		} catch (SQLException e) {
			NeocoreAPI.getLogger().log(Level.SEVERE, "Could not initialize database!", e);
		}
		
	}
	
	private JdbcSession findSession(UUID playerId) {
		
		for (JdbcSession s : this.cache) {
			if (s.getUniqueId().equals(playerId)) return s;
		}
		
		return null;
		
	}

	@Override
	public Session load(UUID uuid) throws IOException {
		
		UUID sessionId = this.playerIdToSessionIdMap.get(uuid);
		JdbcSession session = null;
		
		if (sessionId == null) {
			
			// Oh boy, we need to find the session ID.
			try {
				
				// Prepare the query for the active session.  TODO Fix names as strings.
				QueryBuilder<JdbcSession, UUID> qb = this.sessionDao.queryBuilder();
				qb.where().eq("uuid", uuid).and().eq("state", SessionState.ACTIVE);
				qb.orderBy("start", false); // Get the latest at the top.
				
				// Execute.
				List<JdbcSession> matches = null;
				
				if (NeocoreAPI.isNetworked() && !NeocoreAPI.isFrontend()) {
					
					int tries = 100; // TODO Make configurable.
					
					// If we're an endpoint then we're not supposed to be creating any sessions.
					while (--tries > 0) {
						
						matches = this.sessionDao.query(qb.prepare());
						
						if (matches.size() > 0) {
							
							session = matches.remove(0);
							break;
							
						}
						
						try {
							Thread.sleep(10L); // TODO Make configurable.
						} catch (InterruptedException e) {
							NeocoreAPI.getLogger().warning("Interrupted while waiting for proxy to flush session.");
						}
						
					}
					
					if (tries <= 0) NeocoreAPI.getLogger().warning("Could not query session before retry limit, returning null.");
					
				} else {
					
					// If we're a standalone or a frontend then we can create sessions if we have to.
					matches = this.sessionDao.query(qb.prepare());
					
					if (matches.size() == 0) {
						
						int updated = -1;
						
						try {
							
							JdbcSession created = new JdbcSession(uuid);
							this.sessionDao.create(created);
							session = this.sessionDao.queryForId(created.getSessionId());
							
						} catch (SQLException e) {
							NeocoreAPI.getLogger().log(Level.SEVERE, "Problem when creating session entry!", e);
						}
						
						if (updated != 1) NeocoreAPI.getLogger().warning("Didn't update only 1 row when creating session!  Expect problems!");
						
					} else {
						session = matches.remove(0);
					}
					
				}
				
				// Handle any extras.
				if (matches.size() > 0) {
					
					NeocoreAPI.getLogger().warning("Found " + matches.size() + " old active sessions for " + uuid + ", cleaning...");
					
					// Cleanup states on the remaining.
					matches.forEach(extra -> {
						
						// Cleanup bad sessions.
						extra.setState(SessionState.UNKNOWN);
						
						// Then commit.
						try {
							this.sessionDao.update(extra);
						} catch (SQLException e) {
							
							try {
								NeocoreAPI.getLogger().warning("Failed to update bad session, deleting...");
								this.sessionDao.deleteById(extra.getSessionId());
							} catch (SQLException e1) {
								NeocoreAPI.getLogger().log(Level.SEVERE, "Failed to delete bad session!", e1);
							}
							
						}
						
					});
					
				}
				
			} catch (SQLException e) {
				NeocoreAPI.getLogger().log(Level.SEVERE, "Problem loading session data! (" + uuid + ")", e);
			}
			
		} else {
			
			// In this case, we know the session ID.
			try {
				session = this.sessionDao.queryForId(sessionId);
			} catch (SQLException e) {
				NeocoreAPI.getLogger().log(Level.SEVERE, "Problem loading session data! (" + uuid + ")", e);
			}
			
		}
		
		if (session != null) {
			
			this.cache.add(session);
			this.playerIdToSessionIdMap.put(uuid, session.getSessionId());
			
		}
		
		return session;
		
	}
	
	@Override
	public void flush(UUID uuid) throws IOException {
		
		JdbcSession sess = this.findSession(uuid);
		
		if (sess != null) {
			
			try {
				
				sess.cleanupCachedSaves();
				this.sessionDao.createOrUpdate(sess);
				sess.setDirty(false);
				
			} catch (SQLException e) {
				NeocoreAPI.getLogger().log(Level.WARNING, "Problem flushing session to database! (" + uuid + ")", e);
			}
			
		} else {
			throw new IllegalStateException("Tried to flush a session we do not have a copy of.");
		}
		
	}
	
	@Override
	public void invalidate(UUID uuid) {
		
		JdbcSession sess = this.findSession(uuid);
		if (sess != null) {
			
			sess.invalidate();
			this.cache.remove(sess);
			
		}
		
	}
	
	@Override
	public void unload(UUID uuid) {
		
		JdbcSession sess = this.findSession(uuid);
		
		// Invalidate the object and remove from caches, etc.
		sess.invalidate();
		this.cache.remove(sess);
		this.playerIdToSessionIdMap.remove(uuid);
		
	}
	
}
