package io.neocore.api.database.ban;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import io.neocore.api.host.Context;

/**
 * A list of bans that a player can have.
 * 
 * @author treyzania
 */
public class BanList implements Iterable<BanEntry> {
	
	private UUID uuid;
	private List<BanEntry> bans;
	
	public BanList(UUID uuid) {
		
		this.uuid = uuid;
		this.bans = new ArrayList<>();
		
	}
	
	/**
	 * Adds a ban to the collection of bans for the player.
	 * 
	 * @param entry The ban to be added.
	 */
	public void addBan(BanEntry entry) {
		
		if (this.uuid.equals(entry.getUniqueId())) throw new IllegalArgumentException("Tried to add ban to incompatible ban list.");
		this.bans.add(entry);
		
	}
	
	/**
	 * @return <code>true</code> if there are active global bans, <code>false</code> otherwise.
	 */
	public boolean hasActiveGlobalBans() {
		
		for (BanEntry ban : this.bans) {
			if (ban.isActive() && ban.isGlobal()) return true;
		}
		
		return false;
		
	}
	
	/**
	 * Checks to see if there are acitve bans in the speficied context.
	 * 
	 * @param context The context to check for bans in.
	 * @return <code>true</code> if there are active contextual bans, <code>false</code> otherwise.
	 */
	public boolean isBannedInContext(Context context) {
		
		for (BanEntry ban : this.bans) {
			if (ban.isActive() && ban.getContext().equals(context)) return true;
		}
		
		return false;
		
	}
	
	/**
	 * @return A list of active global bans.
	 */
	public List<BanEntry> getActiveGlobalBans() {
		
		List<BanEntry> actives = new ArrayList<>();
		
		for (BanEntry ban : this.bans) {
			if (ban.isActive() && ban.isGlobal()) actives.add(ban);
		}
		
		return actives;
		
	}
	
	/**
	 * @param context The context to collect bans in for.
	 * @return A list of bans applicable in that context.
	 */
	public List<BanEntry> getActiveBansInContext(Context context) {
		
		List<BanEntry> actives = new ArrayList<>();
		
		for (BanEntry ban : this.bans) {
			if (ban.isActive() && ban.getContext().equals(context)) actives.add(ban);
		}
		
		return actives;
		
	}
	
	/**
	 * @return The total number of active bans.
	 */
	public int getOverallActiveCount() {
		
		int count = 0;
		
		for (BanEntry ban : this.bans) {
			if (ban.isActive()) count++;
		}
		
		return count;
		
	}
	
	/**
	 * @return The UUID that this ban list is for.
	 */
	public UUID getUniqueId() {
		return this.uuid;
	}
	
	@Override
	public Iterator<BanEntry> iterator() {
		return this.bans.iterator();
	}
	
}
