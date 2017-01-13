package io.neocore.bungee;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.typesafe.config.Config;

import io.neocore.api.NeocoreConfig;
import io.neocore.api.PlayerIoThreadingModel;
import io.neocore.api.host.Context;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeeNeocoreConfig implements NeocoreConfig {
	
	public static final String CONFIG_FILE_NAME = "neocore.conf";
	
	private String serverName, networkName;
	
	private boolean enforceBans;
	
	private Context primaryContext;
	private List<Context> contexts;

	private static final String pIoModel_key = "server.playerThreadingModel";
	private PlayerIoThreadingModel pIoModel = PlayerIoThreadingModel.AUTO_DEFERRED;
	
	public BungeeNeocoreConfig(Config conf) {
		
		this.serverName = conf.getString("server.name");
		this.networkName = conf.getString("server.network");
		
		this.enforceBans = conf.getBoolean("server.enforceBans");

		if (conf.hasPath(pIoModel_key)) this.pIoModel = PlayerIoThreadingModel.valueOf(conf.getString(pIoModel_key));
		
		this.primaryContext = Context.create(conf.getString("context.primary"));
		this.contexts = new ArrayList<>();
		this.contexts.add(this.primaryContext);
		
		List<String> ctxStrs = conf.getStringList("context.secondaries");
		for (String ctx : ctxStrs) {
			this.contexts.add(Context.create(ctx));
		}
		
	}
	
	@Override
	public String getServerName() {
		return this.serverName;
	}
	
	@Override
	public String getNetworkName() {
		return this.networkName;
	}

	@Override
	public boolean isEnforcingBans() {
		return this.enforceBans;
	}

	@Override
	public boolean isNetworked() {
		return true;
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
			
			InputStream is = plugin.getResourceAsStream(CONFIG_FILE_NAME);
			
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

	@Override
	public PlayerIoThreadingModel getPlayerThreadingModel() {
		return this.pIoModel;
	}
	
}
