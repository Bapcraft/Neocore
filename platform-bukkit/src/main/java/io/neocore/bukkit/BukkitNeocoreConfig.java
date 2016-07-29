package io.neocore.bukkit;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import org.bukkit.plugin.Plugin;

import com.typesafe.config.Config;

import io.neocore.api.NeocoreConfig;

public class BukkitNeocoreConfig implements NeocoreConfig {
	
	public static final String CONFIG_FILE_NAME = "neocore.conf";
	
	private String serverName;
	
	private boolean enforceBans;
	private boolean networked;
	
	public BukkitNeocoreConfig(Config conf) {
		
		this.serverName = conf.getString("server.name");
		
		this.enforceBans = conf.getBoolean("server.enforceBans");
		this.networked = conf.getBoolean("server.isBungeecord");
		
	}
	
	@Override
	public String getServerName() {
		return this.serverName;
	}
	
	@Override
	public boolean isEnforcingBans() {
		return this.enforceBans;
	}

	@Override
	public boolean isNetworked() {
		return this.networked;
	}
	
	@SuppressWarnings("resource")
	protected static void verifyConfig(File config, Plugin plugin) {
		
		if (!config.exists()) {
			
			InputStream is = plugin.getResource(CONFIG_FILE_NAME);
			
			// Pull the data out.
			Scanner s = new Scanner(is).useDelimiter("\\A");
			String data = s.hasNext() ? s.next() : "error {\n\terror=\"ERROR\"\n}\n";
			s.close();
			
			try {
				
				// Ensure that the parents all exist.
				config.getParentFile().mkdirs();
				
				// Then write it.
				FileWriter fw = new FileWriter(config);
				fw.write(data);
				fw.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
}
