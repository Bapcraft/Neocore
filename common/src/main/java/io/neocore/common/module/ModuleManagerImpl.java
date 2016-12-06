package io.neocore.common.module;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import com.treyzania.jzania.ExoContainer;
import com.treyzania.jzania.data.MemberReferenceSet;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.module.Micromodule;
import io.neocore.api.module.Module;
import io.neocore.api.module.ModuleManager;
import io.neocore.common.module.micro.MicromoduleLoader;

public class ModuleManagerImpl implements ModuleManager {
	
	private ExoContainer container;
	
	private Set<Module> modules;
	private boolean open = true;
	
	@SuppressWarnings("unused")
	private MicromoduleLoader micromoduleLoader;
	
	public ModuleManagerImpl(File micromoduleDir) {
		
		this.modules = new MemberReferenceSet<Module>(m -> m.getName().hashCode());
		
		// Validate the micromodule dir, first
		if (!micromoduleDir.exists()) micromoduleDir.mkdirs();
		
		this.micromoduleLoader = new MicromoduleLoader(this, micromoduleDir);
		
		this.container = new ExoContainer(NeocoreAPI.getLogger());
		
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
		
		log.fine("Configuring micromodules...");
		Set<Micromodule> ok = new HashSet<>();
		this.modules.forEach(m -> {
			
			if (m instanceof Micromodule) {
				
				this.container.invoke(String.format("MicromoduleConfigure(%s)", m.getName()), () -> {
					
					File f = new File(m.getName() + ".conf");
					
					if (f.exists()) {
						
						Config config = ConfigFactory.parseFile(f);
						((Micromodule) m).configure(config);
						
					} else {
						
						NeocoreAPI.getLogger().warning("No general config found for " + m.getName() + ", passing null...");
						((Micromodule) m).configure(null); // Hope everything goes well.
						
					}
					
					ok.add((Micromodule) m);
					
				});
				
			}
			
		});
		
		log.fine("Enabling micromodules...");
		ok.forEach(m -> {
			
			log.finer("Enabling " + m.getName() + " v" + m.getVersion() + "...");
			
			this.container.invoke(String.format("MicromoduleEnable(%s)", m.getName()), () -> {
				m.onEnable();
			});
			
			log.finer("Micromodule " + m.getName() + " enabled!");
			
		});
		
	}
	
}
