package io.neocore.bukkit.services.permissions;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.host.permissions.PermissedPlayer;
import io.neocore.api.host.permissions.PermissionCollection;

public class BukkitPermPlayer implements PermissedPlayer {
	
	private Plugin plugin;
	private Player player;
	
	public BukkitPermPlayer(Plugin plugin, Player p) {
		
		this.plugin = plugin;
		this.player = p;
		
	}
	
	@Override
	public UUID getUniqueId() {
		return this.player.getUniqueId();
	}
	
	@Override
	public boolean isOp() {
		return this.player.isOp();
	}
	
	@Override
	public boolean hasPermission(String perm) {
		return this.player.hasPermission(perm);
	}
	
	@Override
	public boolean isPermSet(String permission) {
		return this.player.isPermissionSet(permission);
	}
	
	@Override
	public PermissionCollection createCollection() {
		return new AttachmentWrapperCollection(this.player.addAttachment(this.plugin));
	}
	
	@Override
	public void removeCollection(PermissionCollection col) {
		
		if (col instanceof AttachmentWrapperCollection) {
			this.player.removeAttachment(((AttachmentWrapperCollection) col).getAttachment());
		} else {
			NeocoreAPI.getLogger().warning("Um, someone tried to remove an attachment from player " + this.getUniqueId() + ", but it doesn't seem like they passed in an AttachmentWrapperCollection that we like.");
		}
		
	}
	
}
