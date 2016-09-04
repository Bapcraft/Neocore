package io.neocore.api.gameplay;

import java.util.List;

import io.neocore.api.player.NeoPlayer;

public interface ScoreboardEntry {
	
	public boolean needsUpdate();
	public List<String> getValue(NeoPlayer player);
	
}
