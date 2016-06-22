package io.neocore;

import java.util.UUID;

import io.neocore.database.DatabaseController;
import io.neocore.host.HostPlugin;
import io.neocore.player.NeoPlayer;

public class NeocoreImpl implements Neocore {

	public HostPlugin getHost() {
		return null; // TODO Set up system to set plugin instance.
	}

	@Override
	public DatabaseController getDatabase() {
		return null;
	}

	@Override
	public NeoPlayer getPlayer(UUID uuid) {
		// TODO Auto-generated method stub
		return null;
	}

}
