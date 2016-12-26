package io.neocore.jdbc.group;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.database.group.GroupService;
import io.neocore.api.player.group.Group;
import io.neocore.jdbc.AbstractJdbcService;

public class JdbcGroupService extends AbstractJdbcService implements GroupService {
	
	private Dao<JdbcGroup, UUID> groupDao;
	
	private List<JdbcGroup> cache = new ArrayList<>();
	
	public JdbcGroupService(ConnectionSource src) {
		
		super(src);
		
	}
	
	@Override
	public void init() {
		
		try {
			
			this.groupDao = DaoManager.createDao(this.getSource(), JdbcGroup.class);
			DaoManager.createDao(this.getSource(), JdbcFlair.class);
			DaoManager.createDao(this.getSource(), JdbcPermissionEntry.class);
			
			TableUtils.createTableIfNotExists(this.getSource(), JdbcGroup.class);
			TableUtils.createTableIfNotExists(this.getSource(), JdbcFlair.class);
			TableUtils.createTableIfNotExists(this.getSource(), JdbcPermissionEntry.class);
			
		} catch (SQLException e) {
			NeocoreAPI.getLogger().log(Level.SEVERE, "Problem initializing group management!", e);
		}
		
	}
	
	@Override
	public void finish() {
		
		try {
			
			// Cleanup the "UNSET" values.
			DeleteBuilder<JdbcGroup, UUID> deleter = this.groupDao.deleteBuilder();
			deleter.where().eq("state", PermState.UNSET.name());
			this.groupDao.delete(deleter.prepare());
			
		} catch (SQLException e) {
			NeocoreAPI.getLogger().log(Level.WARNING, "Cleanup problem.", e);
		}
		
	}

	@Override
	public Group createGroup(String name) {
		
		try {
			
			// Make the local copy to work with.
			JdbcGroup created = new JdbcGroup(name);
			
			// Hook up callback.
			created.setFlushProcedure(() -> {
				
				try {
					
					this.groupDao.createOrUpdate(created);
					created.setDirty(false);
					
				} catch (SQLException e) {
					NeocoreAPI.getLogger().log(Level.WARNING, "Problem flushing group to database for the first time.", e);
				}
				
			});
			
			// Scary bit here.
			this.groupDao.create(created);
			
			// Update the cache and return.
			this.cache.add(created);
			return created;
			
		} catch (SQLException e) {
			NeocoreAPI.getLogger().log(Level.WARNING, "Problem adding new group to database.", e);
		}
		
		return null;
		
	}

	@Override
	public boolean deleteGroup(Group group) {
		
		try {
			
			// Get our version of the group.
			JdbcGroup needed = this.getGroup(group.getName());
			if (needed == null) return false;
			
			// Now actually delete it.
			int done = this.groupDao.delete(needed);
			if (done == 1) {
				
				this.cache.remove(needed);
				return true;
				
			}
			
		} catch (SQLException e) {
			NeocoreAPI.getLogger().log(Level.WARNING, "Problem deleteing group in database.", e);
		}
		
		return false;
		
	}
	
	public JdbcGroup getGroup(String name) {
		
		for (JdbcGroup jg : this.cache) {
			if (jg.getName().equals(name)) return jg;
		}
		
		return null;
		
	}
	
	@Override
	public List<Group> loadGroups() {
		
		List<Group> out = new ArrayList<>();
		
		try {
			
			// Pretty much just load the cache from the database.
			this.cache = this.groupDao.queryForAll();
			
			// Set up the flush callback.
			for (JdbcGroup jg : this.cache) {
				
				jg.setFlushProcedure(() -> {
					
					try {
						
						this.groupDao.update(jg);
						jg.setDirty(false);
						
					} catch (SQLException e) {
						NeocoreAPI.getLogger().log(Level.WARNING, "Problem flushing group to database.", e);
					}
					
				});
				
			}
			
			// Fill the output list because of how Java doesn't like auto upcasting generics.
			out.addAll(this.cache);
			
		} catch (SQLException e) {
			NeocoreAPI.getLogger().log(Level.SEVERE, "Problem loading groups from DB.", e);
		}
		
		return out;
		
	}

}
