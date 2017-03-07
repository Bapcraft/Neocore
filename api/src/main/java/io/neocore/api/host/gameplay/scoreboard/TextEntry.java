package io.neocore.api.host.gameplay.scoreboard;

import java.util.Arrays;
import java.util.List;

import io.neocore.api.player.NeoPlayer;

public class TextEntry implements ScoreboardEntry {

	private List<String> text;

	public TextEntry(List<String> lines) {
		this.text = lines;
	}

	public TextEntry(String... lines) {
		this(Arrays.asList(lines));
	}

	@Override
	public boolean needsUpdate() {
		return false;
	}

	@Override
	public List<String> getValue(NeoPlayer player) {
		return this.text;
	}

}
