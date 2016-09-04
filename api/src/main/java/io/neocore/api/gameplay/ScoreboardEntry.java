package io.neocore.api.gameplay;

import io.neocore.api.player.NeoPlayer;

public interface ScoreboardEntry {

	public void getValue(NeoPlayer player);
	
	public boolean needsUpdate();
	
	public void update();
}
