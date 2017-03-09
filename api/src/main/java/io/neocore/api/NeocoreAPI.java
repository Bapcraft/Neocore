package io.neocore.api;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import io.neocore.api.database.DatabaseController;
import io.neocore.api.database.DatabaseManager;
import io.neocore.api.host.Context;
import io.neocore.api.module.Module;
import io.neocore.api.player.NeoPlayer;
import io.neocore.api.player.extension.RegisteredExtension;

/**
 * Main class for the Neocore API.
 * 
 * @author treyzania
 */
public class NeocoreAPI {

	protected static Neocore agent;

	private static final String NEOCORE_LOGGER_NAME = "Neocore";
	protected static Logger logger = Logger.getLogger(NEOCORE_LOGGER_NAME);

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
	 * @return a human-readable version of the Neocore agent services state and
	 *         everything.
	 */
	public static String dumpNeocoreConfigurationState() {

		StringBuilder sb = new StringBuilder();

		sb.append("=== Neocore Status Dump ===\n");

		// Display host info
		sb.append("Host Class: " + agent.getHost().getClass().getName() + "\n");
		sb.append("Host Version: " + agent.getHost().getVersion() + "\n");
		sb.append("Host Server Name: " + agent.getHost().getNeocoreConfig().getServerName() + "\n");
		sb.append("Agent ID: " + agent.getAgentId() + "\n");

		// Display contexts listed
		sb.append("Host Contexts:\n");
		List<Context> contexts = agent.getHost().getContexts();
		for (Context c : contexts) {
			sb.append(" - " + c.getName() + "\n");
		}

		// Display module info
		sb.append("Registered Modules:\n");
		Set<Module> mods = agent.getModuleManager().getModules();
		for (Module m : mods) {
			sb.append(String.format(" - %s v%s (%s)\n", m.getName(), m.getVersion(), m.getModuleType().name()));
		}

		// List extensions registered
		sb.append("Registered Extensions:\n");
		List<RegisteredExtension> exts = agent.getExtensionManager().getTypes();
		for (RegisteredExtension reg : exts) {
			sb.append(String.format(" - %s (%s)\n", reg.getName(), reg.getExtensionClass().getSimpleName()));
		}

		// Display database engines
		sb.append("Database Controllers:\n");
		DatabaseManager dbm = agent.getDatabaseManager();
		for (Class<? extends DatabaseController> dbc : dbm.getControllerClasses()) {
			sb.append(" - " + dbc.getSimpleName() + "\n");
		}

		// Display service info
		sb.append("Registered Services:\n");
		List<RegisteredService> servs = agent.getServiceManager().getServices();
		for (RegisteredService rs : servs) {
			ServiceType st = rs.getType();
			sb.append(String.format(" - %s: %s %s (%s)\n", st.getName(), rs.getModule().getName(),
					rs.getServiceProvider().getClass().getSimpleName(), st.getClass().getSimpleName()));
		}

		// List off all the other services that need to be dealt with eventually
		sb.append("Unprovisioned Services:\n");
		List<ServiceType> types = agent.getServiceManager().getUnprovidedServices();
		for (ServiceType st : types) {
			sb.append(String.format(" - %s (%s)\n", st.getName(), st.getClass().getSimpleName()));
		}

		sb.append("===========================");
		return sb.toString();

	}

	/**
	 * Logs all the current state of Neocore services and state to the logger
	 * provided.
	 */
	public static void announceCompletion() {
		logger.info(dumpNeocoreConfigurationState());
	}

	/**
	 * Convenience method.
	 * 
	 * @return <code>true<code> if players connect directly to this server, <code>false</code>
	 *         if it's behind a proxy.
	 */
	public static boolean isFrontend() {
		return getAgent().getHost().isFrontServer();
	}

	/**
	 * Convenience method.
	 * 
	 * @return If we're part of a network.
	 */
	public static boolean isNetworked() {
		return getAgent().isNetworked();
	}

	/**
	 * Convenience method.
	 * 
	 * @return The server's name.
	 */
	public static String getServerName() {
		return getAgent().getHost().getNeocoreConfig().getServerName();
	}

	/**
	 * Convenience method.
	 * 
	 * @return The server's network.
	 */
	public static String getNetworkName() {
		return getAgent().getHost().getNeocoreConfig().getNetworkName();
	}

	/**
	 * Gets a NeoPlayer based on their UUID.
	 * 
	 * @param uuid
	 *            The player's UUID.
	 * @return The NeoPlayer, if available.
	 */
	public static NeoPlayer getPlayer(UUID uuid) {
		return getAgent().getPlayerManager().getPlayer(uuid);
	}

	/**
	 * Gets an online NeoPlayer based on their username.
	 * 
	 * @param name
	 *            The player's username.
	 * @return The NeoPlayer, if available.
	 */
	public static NeoPlayer getPlayer(String name) {

		Set<NeoPlayer> players = getAgent().getPlayerManager().getOnlinePlayers();

		for (NeoPlayer np : players) {
			if (np.getUsername().equalsIgnoreCase(name))
				return np;
		}

		return null;

	}

	static {

		Logger log = Logger.getLogger(NEOCORE_LOGGER_NAME);

		try {

			FileHandler lh = new FileHandler("neocore.log");
			log.addHandler(lh);
			lh.setFormatter(new NeocoreLogFormatter());

		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
