package io.neocore.jdbc.artifact;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.database.artifact.Artifact;
import io.neocore.api.database.artifact.ArtifactService;
import io.neocore.jdbc.AbstractJdbcService;

public class JdbcArtifactService extends AbstractJdbcService implements ArtifactService {
	
	private Dao<JdbcArtifact, UUID> artifactDao;
	
	public JdbcArtifactService(ConnectionSource src) {
		super(src);
	}
	
	@Override
	public void init() {
		
		try {
			
			this.artifactDao = DaoManager.createDao(this.getSource(), JdbcArtifact.class);
			TableUtils.createTableIfNotExists(this.getSource(), JdbcArtifact.class);
			
		} catch (SQLException e) {
			NeocoreAPI.getLogger().log(Level.SEVERE, "Problem setting up artifact DB connection.", e);
		}
		
	}

	@Override
	public Artifact createArtifact(UUID owner, String type, String data) {
		
		try {
			
			JdbcArtifact art = new JdbcArtifact(owner, type, data);
			this.artifactDao.create(art);
			this.addCallback(art);
			return art;
			
		} catch (SQLException e) {
			NeocoreAPI.getLogger().log(Level.SEVERE, "Problem creating artifact!", e);
		}
		
		return null;
		
	}

	@Override
	public Artifact getArtifact(UUID uuid) {
		
		try {
			
			JdbcArtifact art = this.artifactDao.queryForId(uuid);
			this.addCallback(art);
			return art;
			
		} catch (SQLException e) {
			NeocoreAPI.getLogger().log(Level.SEVERE, "Could not query artifact!", e);
		}
		
		return null;
		
	}

	@Override
	public List<Artifact> getArtifacts(String type) {
		
		try {
			
			List<Artifact> out = new ArrayList<>();
			List<JdbcArtifact> arts = this.artifactDao.queryForEq("type", type);
			arts.forEach(a -> this.addCallback(a));
			out.addAll(arts);
			return out;
			
		} catch (SQLException e) {
			NeocoreAPI.getLogger().log(Level.SEVERE, "Could not query artifacts properly!", e);
		}
		
		return null;
		
	}
	
	@Override
	public List<Artifact> getArtifacts(UUID ownerId) {
		
		try {
			
			List<Artifact> out = new ArrayList<>();
			List<JdbcArtifact> arts = this.artifactDao.queryForEq("ownerId", ownerId);
			arts.forEach(a -> this.addCallback(a));
			out.addAll(arts);
			return out;
			
		} catch (SQLException e) {
			NeocoreAPI.getLogger().log(Level.SEVERE, "Could not query artifacts properly!", e);
		}
		
		return null;
		
	}
	
	private void addCallback(JdbcArtifact art) {
		
		art.setFlushProcedure(() -> {
			
			try {
				this.artifactDao.update(art);
			} catch (SQLException e) {
				NeocoreAPI.getLogger().log(Level.SEVERE, "Problem flushing artifact!", e);
			}
			
		});
		
	}
	
}
