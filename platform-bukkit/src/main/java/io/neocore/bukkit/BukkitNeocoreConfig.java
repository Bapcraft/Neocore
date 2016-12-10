package io.neocore.bukkit;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.bukkit.plugin.Plugin;

import com.typesafe.config.Config;

import io.neocore.api.NeocoreConfig;
import io.neocore.api.host.Context;
import io.neocore.api.host.LesserContext;

public class BukkitNeocoreConfig implements NeocoreConfig {
	
	public static final String CONFIG_FILE_NAME = "neocore.conf";
	
	private String serverName, networkName;
	
	private boolean enforceBans;
	private boolean networked;
	
	private Context primaryContext;
	private List<Context> contexts;
	
	public BukkitNeocoreConfig(Config conf) {
		
		this.serverName = conf.getString("server.name");
		this.networkName = conf.getString("server.network");
		
		this.enforceBans = conf.getBoolean("server.enforceBans");
		this.networked = conf.getBoolean("server.isBungeecord");
		
		this.primaryContext = new LesserContext(conf.getString("context.primary"));
		this.contexts = new ArrayList<>();
		this.contexts.add(this.primaryContext);
		
		List<String> ctxStrs = conf.getStringList("context.secondaries");
		for (String ctx : ctxStrs) {
			this.contexts.add(new LesserContext(ctx));
		}
		
	}
	
	@Override
	public String getServerName() {
		return this.serverName;
	}
	
	@Override
	public String getNetworkName() {
		return this.isNetworked() ? this.networkName : null;
	}

	@Override
	public boolean isEnforcingBans() {
		return this.enforceBans;
	}

	@Override
	public boolean isNetworked() {
		return this.networked;
	}
	
	@Override
	public Context getPrimaryContext() {
		return this.primaryContext;
	}

	@Override
	public List<Context> getContexts() {
		return this.contexts;
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
