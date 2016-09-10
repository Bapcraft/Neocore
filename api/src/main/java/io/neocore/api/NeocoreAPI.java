package io.neocore.api;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import io.neocore.api.database.DatabaseController;
import io.neocore.api.database.DatabaseManager;
import io.neocore.api.host.Context;
import io.neocore.api.module.Module;
import io.neocore.api.player.extension.RegisteredExtension;

/**
 * Main class for the Neocore API.
 * 
 * @author treyzania
 */
public class NeocoreAPI {
	
	protected static Neocore agent;
	protected static Logger logger;
	
	/**
	 * @return The Neocore instance managing all of the suite
	 */
	public static Neocore getAgent() {
		return agent;
	}
	
	/**
	 * @return The server-provided logger
	 */
	public static Logger getLogger() {
		return logger;
	}
	
	/**
	 * Logs all the current state of Neocore services and state to the logger provided.
	 */
	public static void announceCompletion() {
		
		logger.info("=== Neocore Initialization Complete ===");
		
		// Display host info
		logger.info("Host Class: " + agent.getHost().getClass().getName());
		logger.info("Host Version: " + agent.getHost().getVersion());
		logger.info("Host Server Name: " + agent.getHost().getNeocoreConfig().getServerName());
		
		// Display contexts listed
		logger.info("Host Contexts:");
		List<Context> contexts = agent.getHost().getContexts();
		for (Context c : contexts) {
			logger.info(" - " + c.getName());
		}
		
		// Display module info
		logger.info("Registered Modules:");
		Set<Module> mods = agent.getModuleManager().getModules();
		for (Module m : mods) {
			logger.info(String.format(" - %s v%s (%s)", m.getName(), m.getVersion(), m.getModuleType().name()));
		}
		
		// List extensions registered
		logger.info("Registered Extensions:");
		List<RegisteredExtension> exts = agent.getExtensionManager().getTypes();
		for (RegisteredExtension reg : exts) {
			logger.info(String.format(" - %s (%s)", reg.getName(), reg.getExtensionClass().getSimpleName()));
		}
		
		// Display database engines
		logger.info("Database Controllers:");
		DatabaseManager dbm = agent.getDatabaseManager();
		for (Class<? extends DatabaseController> dbc : dbm.getControllerClasses()) {
			logger.info(" - " + dbc.getSimpleName());
		}
		
		// Display service info
		logger.info("Registered Services:");
		List<RegisteredService> servs = agent.getServiceManager().getServices();
		for (RegisteredService rs : servs) {
			ServiceType st = rs.getType();
			logger.info(String.format(" - %s: %s %s (%s)", st.getName(), rs.getModule().getName(), rs.getServiceProvider().getClass().getSimpleName(), st.getClass().getSimpleName()));
		}
		
		// List off all the other services that need to be dealt with eventually
		logger.info("Unprovisioned Services:");
		List<ServiceType> types = agent.getServiceManager().getUnprovidedServices();
		for (ServiceType st : types) {
			logger.info(String.format(" - %s (%s)", st.getName(), st.getClass().getSimpleName()));
		}
		
		logger.info("=======================================");
		
	}
	
	/**
	 * Convenience method/
	 * 
	 * @return <code>true<code> if players connect directly to this server, <code>false</code> if it's behind a proxy.
	 */
	public static boolean isFrontend() {
		return getAgent().getHost().isFrontServer();
	}
	
}
