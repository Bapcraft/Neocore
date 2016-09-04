package io.neocore.api.gameplay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ScoreboardDisplay {
	
	private List<ScoreboardEntry> scoreboardEntries = new ArrayList<ScoreboardEntry>();
	
	private String title = "";
	
	private HashMap<UUID, List<String>> cache = new HashMap<>();
	
	public ScoreboardDisplay(String title) {
		this.title = title;
	}
	
	public String getTitle(){
		return title;
	}
	
	public void addScoreboardEntry(ScoreboardEntry sEntry){
		scoreboardEntries.add(sEntry);
	}
	
	public void removeScoreboardEntry(ScoreboardEntry sEntry){
		if(scoreboardEntries.contains(sEntry)){
			scoreboardEntries.remove(sEntry);
		}
	}
	
	public List<ScoreboardEntry> getScoreboardEntries(){
		return scoreboardEntries;
	}
	
	public boolean update(){
		if(scoreboardEntries.size() == 0){ return false; }
		
		for(ScoreboardEntry sEntry : scoreboardEntries){
			if(sEntry.needsUpdate()){
				sEntry.update();
			}
		}
		
		return true;
	}
	
}
