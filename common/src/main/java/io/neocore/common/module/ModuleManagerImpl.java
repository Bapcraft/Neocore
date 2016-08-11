package io.neocore.common.module;

import java.io.File;
import java.util.Set;
import java.util.logging.Logger;

import com.treyzania.jzania.data.MemberReferenceSet;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.module.Micromodule;
import io.neocore.api.module.Module;
import io.neocore.api.module.ModuleManager;
import io.neocore.common.module.micro.MicromoduleLoader;

public class ModuleManagerImpl implements ModuleManager {
	
	private Set<Module> modules;
	private boolean open = true;
	
	@SuppressWarnings("unused")
	private MicromoduleLoader micromoduleLoader;
	
	public ModuleManagerImpl(File micromoduleDir) {
		
		this.modules = new MemberReferenceSet<Module>(m -> m.getName().hashCode());
		
		// Validate the micromodule dir, first
		if (!micromoduleDir.exists()) micromoduleDir.mkdirs();
		
		this.micromoduleLoader = new MicromoduleLoader(this, micromoduleDir);
		
	}
	
	@Override
	public void registerModule(Module mod) {
		this.modules.add(mod);
	}
	
	@Override
	public boolean isAcceptingRegistrations() {
		return this.open;
	}
	
	@Override
	public Set<Module> getModules() {
		return this.modules;
	}
	
	@Override
	public void enableMicromodules() {
		
		final Logger log = NeocoreAPI.getLogger();
		
		log.info("Enabling micromodules...");
		this.modules.forEach(m -> {
			
			if (m instanceof Micromodule) {
				
				log.info("Enabling " + m.getName() + " v" + m.getVersion() + "...");
				((Micromodule) m).onEnable();
				log.info("Micromodule " + m.getName() + " enabled!");
				
			}
			
		});
		
	}
	
}
