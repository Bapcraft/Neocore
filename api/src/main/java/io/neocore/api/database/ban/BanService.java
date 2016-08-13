package io.neocore.api.database.ban;

import java.util.UUID;

import io.neocore.api.database.DatabaseServiceProvider;

public interface BanService extends DatabaseServiceProvider {
	
	public BanList getBans(UUID uuid);
	public void addBan(BanEntry entry);
	public void updateBanList(BanList list);
	
}
