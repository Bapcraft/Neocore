package io.neocore.jdbc.player;

import java.sql.SQLException;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.logging.Level;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.database.DatabaseService;
import io.neocore.api.database.player.DatabasePlayer;
import io.neocore.api.database.player.PlayerService;
import io.neocore.jdbc.AbstractJdbcService;

public class JdbcPlayerService extends AbstractJdbcService implements PlayerService {
	
	private Set<JdbcDbPlayer> cache;
	private Dao<JdbcDbPlayer, UUID> playerDao;
	
	public JdbcPlayerService(ConnectionSource src) {
		
		super(src);
		
		this.cache = new TreeSet<>();
		
	}
	
	@Override
	public void init() {
		
		NeocoreAPI.getAgent().getPlayerManager().addService(DatabaseService.PLAYER);
		
		try {
			
			// Create all of the DAOs we have.
			this.playerDao = DaoManager.createDao(this.getSource(), JdbcDbPlayer.class);
			DaoManager.createDao(this.getSource(), JdbcGroupMembership.class);
			DaoManager.createDao(this.getSource(), JdbcExtensionRecord.class);
			DaoManager.createDao(this.getSource(), JdbcPlayerAccount.class);
			
			// I didn't realize we had to create the tables ourselves.
			TableUtils.createTableIfNotExists(this.getSource(), JdbcDbPlayer.class);
			TableUtils.createTableIfNotExists(this.getSource(), JdbcGroupMembership.class);
			TableUtils.createTableIfNotExists(this.getSource(), JdbcExtensionRecord.class);
			TableUtils.createTableIfNotExists(this.getSource(), JdbcPlayerAccount.class);
			
		} catch (SQLException e) {
			NeocoreAPI.getLogger().log(Level.SEVERE, "Could not initialize database!", e);
		}
		
	}
	
	private JdbcDbPlayer findPlayer(UUID uuid) {
		
		for (JdbcDbPlayer p : this.cache) {
			if (p.getUniqueId().equals(uuid)) return p;
		}
		
		return null;
		
	}
	
	@Override
	public void flush(UUID uuid) {
		
		JdbcDbPlayer dbp = this.findPlayer(uuid);
		
		if (dbp != null) {
			
			try {
				this.playerDao.createOrUpdate(dbp);
			} catch (SQLException e) {
				NeocoreAPI.getLogger().log(Level.WARNING, "Problem flushing player to database! (" + uuid + ")", e);
			}
			
		} else {
			throw new IllegalArgumentException("Tried to flush a player we don't actually have a copy of.");
		}
		
	}
	
	@Override
	public void invalidate(UUID uuid) {
		
		JdbcDbPlayer dbp = this.findPlayer(uuid);
		if (dbp != null) {
			
			dbp.invalidate();
			this.cache.remove(dbp);
			
		}
		
	}
	
	@Override
	public DatabasePlayer load(UUID uuid) {
		
		JdbcDbPlayer p = this.findPlayer(uuid);
		if (p != null) return p;
		
		try {
			
			if (NeocoreAPI.isNetworked() && !NeocoreAPI.isFrontend()) {
				
				int tries = 100; // TODO Make configurable.
				
				while (--tries > 0) {
					
					p = this.playerDao.queryForId(uuid);
					
					if (p != null) break;
					
					try {
						Thread.sleep(10L); // TODO Make configurable.
					} catch (InterruptedException e) {
						NeocoreAPI.getLogger().warning("Interrupted while delaying to requery from database.");
					}
					
				}
				
				if (tries <= 0) NeocoreAPI.getLogger().warning("Could not query player data before retry limit, returning null.");
				
			} else {
				
				p = this.playerDao.queryForId(uuid);
				
				if (p == null) {
					
					p = new JdbcDbPlayer(uuid);
					this.playerDao.create(p);
					
				}
				
			}
			
		} catch (SQLException e) {
			
			NeocoreAPI.getLogger().log(Level.SEVERE, "SQL issue loading player from database! (" + uuid + ")", e);
			return null;
			
		}
		
		if (p != null) this.cache.add(p);
		return p;
		
	}
	
	@Override
	public void unload(UUID uuid) {
		
		JdbcDbPlayer dbp = this.findPlayer(uuid);
		
		if (dbp != null) {
			
			dbp.invalidate();
			this.cache.remove(dbp);
			
		} else {
			NeocoreAPI.getLogger().warning("Tried to unload a player that wasn't loaded! (" + uuid + ")");
		}
		
	}
	
	@Override
	public String getLastUsername(UUID id) {
		
		try {
			return this.playerDao.queryForId(id).getLastUsername();
		} catch (NullPointerException e) {
			return "nobody";
		} catch (SQLException e) {
			
			NeocoreAPI.getLogger().log(Level.WARNING, "Problem loading player last username.", e);
			return String.format("ERROR(%s)", id);
			
		}
		
	}
	
}
