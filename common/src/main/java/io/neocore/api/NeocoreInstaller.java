package io.neocore.api;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.treyzania.jzania.timing.Timer;

import io.neocore.api.Neocore;
import io.neocore.api.NeocoreAPI;

public class NeocoreInstaller {
	
	private static boolean done = false; 
	
	public static void install(Neocore neo) {
		
		if (done && NeocoreAPI.agent != neo) {
			throw new IllegalStateException("Neocore already installed!  Cannot install another one!");
		}
		
		NeocoreAPI.agent = neo;
		
	}
	
	public static void applyLogger(Logger log) {
		
		log.info(">>>NEOCORE LOGGER INSTALLED.");
		
		NeocoreAPI.logger = log;
		
		Timer t = Timer.getTimer();
		t.setLogger(log);
		t.setLevel(Level.INFO);
		t.setNewTokenMessage("[New Timer Token (%s)]");
		
	}
	
	public static void reset() {
		done = false;
	}
	
}
