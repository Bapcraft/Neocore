package io.neocore.api.database.ban;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.neocore.api.host.Context;

public class BanList implements Iterable<BanEntry> {
	
	private List<BanEntry> bans;
	
	public BanList() {
		this.bans = new ArrayList<>();
	}
	
	public boolean hasActiveGlobalBans() {
		
		for (BanEntry ban : this.bans) {
			if (ban.isActive() && ban.isGlobal()) return true;
		}
		
		return false;
		
	}
	
	public boolean isBannedInContext(Context context) {
		
		for (BanEntry ban : this.bans) {
			if (ban.isActive() && ban.getContext().equals(context)) return true;
		}
		
		return false;
		
	}
	
	public List<BanEntry> getActiveGlobalBans() {
		
		List<BanEntry> actives = new ArrayList<>();
		
		for (BanEntry ban : this.bans) {
			if (ban.isActive() && ban.isGlobal()) actives.add(ban);
		}
		
		return actives;
		
	}
	
	public List<BanEntry> getActiveBansInContext(Context context) {
		
		List<BanEntry> actives = new ArrayList<>();
		
		for (BanEntry ban : this.bans) {
			if (ban.isActive() && ban.getContext().equals(context)) actives.add(ban);
		}
		
		return actives;
		
	}
	
	public int getOverallActiveCount() {
		
		int count = 0;
		
		for (BanEntry ban : this.bans) {
			if (ban.isActive()) count++;
		}
		
		return count;
		
	}
	
	@Override
	public Iterator<BanEntry> iterator() {
		return this.bans.iterator();
	}
	
}
