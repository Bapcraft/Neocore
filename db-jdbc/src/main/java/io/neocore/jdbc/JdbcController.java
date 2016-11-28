package io.neocore.jdbc;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigObject;
import com.typesafe.config.ConfigValue;
import com.typesafe.config.ConfigValueType;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.database.DatabaseController;
import io.neocore.api.database.DatabaseService;
import io.neocore.api.database.DatabaseServiceProvider;
import io.neocore.jdbc.artifact.JdbcArtifactService;
import io.neocore.jdbc.ban.JdbcBanService;
import io.neocore.jdbc.group.JdbcGroupService;
import io.neocore.jdbc.player.JdbcPlayerService;
import io.neocore.jdbc.session.JdbcSessionService;

public class JdbcController implements DatabaseController {
	
	private List<DatabaseServiceProvider> services;
	
	private Config config;
	private JdbcPooledConnectionSource source;
	
	public JdbcController(Config config) {
		
		this.services = new ArrayList<>();
		this.config = config;
		
	}
	
	@Override
	public String getBrand() {
		return "JDBC-ORMLite";
	}
	
	@Override
	public void initialize() {
		
		// Set up the authentication.
		String url = this.config.getString("jdbc-url");
		ConfigValue authConf = this.config.getValue("auth");
		
		if (authConf.valueType() == ConfigValueType.OBJECT) {
			
			ConfigObject authObj = (ConfigObject) authConf;
			
			String username = (String) authObj.get("username").unwrapped();
			String password = (String) authObj.get("password").unwrapped();
			
			try {
				this.source = new JdbcPooledConnectionSource(url, username, password);
			} catch (SQLException e) {
				NeocoreAPI.getLogger().log(Level.SEVERE, "Could not init connection to database!", e);
			}
			
		} else if (authConf.valueType() == ConfigValueType.NULL) {
			
			try {
				this.source = new JdbcPooledConnectionSource(url);
			} catch (SQLException e) {
				NeocoreAPI.getLogger().log(Level.SEVERE, "Could not init connection to database!", e);
			}
			
		} else {
			throw new IllegalArgumentException("JDBC auth configuration isn't valid!  Must be an object with \"username\" and \"password\" keys, or null!");
		}
		
		// Set the timeout, if applicable.
		if (this.config.hasPath("timeout")) this.source.setMaxConnectionAgeMillis(this.config.getLong("timeout"));
		
		// Now initialize the actual services, etc.
		this.services.add(new JdbcPlayerService(this.source));
		this.services.add(new JdbcSessionService(this.source));
		this.services.add(new JdbcBanService(this.source));
		this.services.add(new JdbcGroupService(this.source));
		this.services.add(new JdbcArtifactService(this.source));
		
	}
	
	@Override
	public void shutdown() {
		
		try {
			this.source.close();
		} catch (IOException e) {
			NeocoreAPI.getLogger().log(Level.WARNING, "Problem closing connection to database!", e);
		}
		
	}
	
	@Override
	public DatabaseServiceProvider[] provide(DatabaseService[] providing) {
		
		DatabaseServiceProvider[] provs = new DatabaseServiceProvider[providing.length];
		
		for (int i = 0; i < providing.length; i++) {
			
			for (DatabaseServiceProvider prov : this.services) {
				
				// Check if the types match, then stick it in the array.
				if (providing[i].isCompatible(prov)) {
					
					provs[i] = prov;
					break;
					
				}
				
			}
			
		}
		
		return provs;
		
	}
	
}
