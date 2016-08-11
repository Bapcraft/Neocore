package io.neocore.api;

import java.util.logging.Logger;

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
		NeocoreAPI.logger = log;
	}
	
	public static void reset() {
		done = false;
	}
	
}
