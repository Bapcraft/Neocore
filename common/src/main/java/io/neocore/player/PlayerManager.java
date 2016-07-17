package io.neocore.player;

import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import io.neocore.api.player.NeoPlayer;

public class PlayerManager {
	
	public Set<NeoPlayer> playerCache = new TreeSet<>();
	
	public NeoPlayer getPlayer(UUID uuid) {
		
		// Try to find the player if it's there already...
		for (NeoPlayer np : this.playerCache) {
			if (np.getUniqueId().equals(uuid)) return np;
		}
		
		// Otherwise assemble a new one.
		return this.assemblePlayer(uuid);
		
	}
	
	public NeoPlayer assemblePlayer(UUID uuid) {
		
		NeoPlayer np = new NeoPlayer(uuid);
		
		// TODO
		
		this.playerCache.add(np);
		return np;
		
	}
	
}
