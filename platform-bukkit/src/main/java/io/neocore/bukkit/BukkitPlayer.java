package io.neocore.bukkit;

import java.util.Date;
import java.util.UUID;

import org.bukkit.OfflinePlayer;
import io.neocore.api.NeocoreAPI;
import io.neocore.api.host.chat.ChattablePlayer;
import io.neocore.api.host.login.ServerPlayer;
import net.md_5.bungee.api.ChatColor;

public class BukkitPlayer extends WrappedPlayer implements ServerPlayer, ChattablePlayer {
	
	public BukkitPlayer(UUID uuid) {
		super(uuid);
	}
	
	@Override
	public String getName() {
		
		OfflinePlayer op = this.getOfflinePlayer();
		return op.hasPlayedBefore() ? op.getName() : "[unknown]";
		
	}

	@Override
	public void setDisplayName(String name) {
		this.getPlayerOrThrow().setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
	}

	@Override
	public String getDisplayName() {
		return ChatColor.stripColor(this.getPlayerOrThrow().getDisplayName());
	}

	@Override
	public void sendMessage(String message) {
		
		try {
			this.getPlayerOrThrow().sendMessage(message);
		} catch (UnsupportedOperationException e) {
			NeocoreAPI.getLogger().warning("Tried to send message to offline player.");
		}
		
	}

	@Override
	public Date getLoginTime() {
		return new Date(this.getOfflinePlayer().getLastPlayed()); // TODO Not what we want, should make a record when they log in and record that.
	}
	
}
