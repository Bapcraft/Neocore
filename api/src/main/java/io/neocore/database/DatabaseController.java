package io.neocore.database;

import java.util.List;

import io.neocore.module.Module;
import io.neocore.module.ModuleType;

public interface DatabaseController extends Module {
	
	/**
	 * @return The name of the database this controller accesses.
	 */
	public String getBrand();
	
	/**
	 * Called when the database connection should be initialized.
	 */
	public void onEnable();
	
	/**
	 * Called when the database connection should be closed and the library should be shut down.
	 */
	public void onDisable();
	
	/**
	 * @return A list of services that this database provider can use.
	 */
	public List<DatabaseServiceProvider> getDatabaseServices();
	
	@Override
	default ModuleType getModuleType() {
		return ModuleType.DATABASE;
	}
	
}
