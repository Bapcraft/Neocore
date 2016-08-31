package io.neocore.api.module;

/**
 * Superclass for micomodule main classes written in Java.
 * 
 * @author treyzania
 */
public abstract class JavaMicromodule implements Micromodule {
	
	private String name, version;

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getVersion() {
		return this.version;
	}

	@Override
	public ModuleType getModuleType() {
		return ModuleType.MICROMODULE;
	}
	
}
