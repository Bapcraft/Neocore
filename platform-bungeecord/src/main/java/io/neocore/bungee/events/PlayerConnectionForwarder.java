package io.neocore.bungee.events;

import io.neocore.common.NeocoreImpl;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.event.EventHandler;

public class PlayerConnectionForwarder extends EventForwarder {
	
	private NeocoreImpl neocore;
	
	public PlayerConnectionForwarder(NeocoreImpl neo) {
		this.neocore = neo;
	}
	
	@EventHandler
	public void onPreLogin(PreLoginEvent event) {
		
		/*
		 * If a player connects before the task that initializes Neocore
		 * quickly after starting the server then we need to initialize it
		 * ourselves.  If it's already being inited when when this call
		 * actually enters it will return immediately.
		 */
		this.neocore.init();
		
		// TODO
		
	}
	
}
