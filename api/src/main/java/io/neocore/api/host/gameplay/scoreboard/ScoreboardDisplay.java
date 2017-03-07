package io.neocore.api.host.gameplay.scoreboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import io.neocore.api.player.NeoPlayer;

public class ScoreboardDisplay {

	private String title;
	private List<ScoreboardEntry> entries;

	private Map<UUID, List<String>> cache; // Index by UUID to make memory leaks
											// minimal.

	public ScoreboardDisplay(String title) {

		this.title = title;

		this.entries = new ArrayList<>();
		this.cache = new HashMap<>();

	}

	public String getTitle() {
		return this.title;
	}

	public void addEntry(ScoreboardEntry entry) {
		this.entries.add(entry);
	}

	public void insertEntry(int index, ScoreboardEntry entry) {
		this.entries.add(index, entry);
	}

	public void removeEntry(ScoreboardEntry entry) {
		this.entries.remove(entry);
	}

	public List<ScoreboardEntry> getScoreboardEntries() {
		return this.entries;
	}

	public List<String> getLines(NeoPlayer player) {

		UUID id = player.getUniqueId();
		List<String> lines = null;

		// Check all of the entries to see if we need to update anything.
		for (ScoreboardEntry entry : this.entries) {

			if (entry.needsUpdate()) {

				// Instantiate and fill it.
				lines = new ArrayList<>();
				for (ScoreboardEntry e2 : this.entries) {
					lines.addAll(e2.getValue(player));
				}

				// Update the cache and break out of the loop.
				this.cache.put(id, lines);
				break;

			}

		}

		// At this point we know that we don't have to update anything, so pull
		// from the cache.
		if (lines == null)
			lines = this.cache.get(id);

		return lines;

	}

	public void purge(UUID uuid) {
		this.cache.remove(uuid);
	}

}
