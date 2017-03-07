package io.neocore.api;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.treyzania.jzania.timing.Timer;

import io.neocore.common.NeocoreImpl;

public class NeocoreInstaller {

	private static boolean done = false;

	public static NeocoreImpl installed;

	public static void install(NeocoreImpl neo) {

		if (done && NeocoreAPI.agent != neo) {
			throw new IllegalStateException("Neocore already installed!  Cannot install another one!");
		}

		NeocoreAPI.agent = neo;
		installed = neo;

	}

	public static void applyLogger(Logger log) {

		log.info(">>>>NEOCORE LOGGER INSTALLED.");

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
