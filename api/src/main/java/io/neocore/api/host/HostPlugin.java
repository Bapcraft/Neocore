package io.neocore.api.host;

import java.io.File;
import java.util.List;

import io.neocore.api.NeocoreConfig;
import io.neocore.api.cmd.AbstractCommand;
import io.neocore.api.module.Module;
import io.neocore.api.module.ModuleType;

/**
 * Represents the plugin class used by whichever server is hosting a Neocore
 * instance.
 * 
 * @author treyzania
 */
public interface HostPlugin extends Module {

	/**
	 * @return The directory to place micrmodules to be loaded.
	 */
	public File getMicromoduleDirectory();

	/**
	 * @return The configuration loaded from file about how Neocore is
	 *         configured currently.
	 */
	public NeocoreConfig getNeocoreConfig();

	/**
	 * @return The database configuration file.
	 */
	public File getDatabaseConfigFile();

	@Override
	public default ModuleType getModuleType() {
		return ModuleType.HOST;
	}

	/**
	 * @return The primary context of this server.
	 */
	public Context getPrimaryContext();

	/**
	 * @return The total list of contexts present on this server.
	 */
	public List<Context> getContexts();

	/**
	 * Registers the specified command to be used on the server.
	 * 
	 * @param cmd
	 *            The command.
	 */
	public void registerCommand(AbstractCommand cmd);

	/**
	 * @return <code>true</code> if this server is something that players
	 *         connect directly to, <code>false</code> if its behind a proxy.
	 */
	public boolean isFrontServer();

	/**
	 * Gets the server's main scheduling service.
	 * 
	 * @return The scheduler.
	 */
	public Scheduler getScheduler();

}
