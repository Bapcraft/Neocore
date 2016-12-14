package io.neocore.api.module;

import io.neocore.api.Neocore;

/**
 * Superclass for micomodule main classes written in Java.
 * 
 * @author treyzania
 */
public abstract class JavaMicromodule implements Micromodule {
	
	private String name, version;
	private Neocore neocore;
	
	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public String getVersion() {
		return this.version;
	}
	
	@Override
	public Neocore getAgent() {
		return this.neocore;
	}
	
	@Override
	public ModuleType getModuleType() {
		return ModuleType.MICROMODULE;
	}
	
}
