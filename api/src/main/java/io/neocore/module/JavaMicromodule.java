package io.neocore.module;

public abstract class JavaMicromodule implements Module {
	
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
