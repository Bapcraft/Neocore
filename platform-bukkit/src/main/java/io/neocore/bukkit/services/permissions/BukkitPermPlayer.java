package io.neocore.bukkit.services.permissions;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.plugin.Plugin;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.host.permissions.PermissedPlayer;
import io.neocore.api.host.permissions.PermissionCollection;
import io.neocore.bukkit.WrappedPlayer;

public class BukkitPermPlayer extends WrappedPlayer implements PermissedPlayer {
	
	private Plugin plugin;
	private List<AttachmentWrapperCollection> collections = new ArrayList<>();
	
	public BukkitPermPlayer(Plugin plugin, UUID uuid) {
		
		super(uuid);
		this.plugin = plugin;
		
	}
	
	@Override
	public boolean isOp() {
		return this.getPlayerOrThrow().isOp();
	}
	
	@Override
	public boolean hasPermission(String perm) {
		return this.getPlayerOrThrow().hasPermission(perm);
	}
	
	@Override
	public boolean isPermSet(String permission) {
		return this.getPlayerOrThrow().isPermissionSet(permission);
	}
	
	@Override
	public PermissionCollection createCollection() {
		
		AttachmentWrapperCollection col = new AttachmentWrapperCollection(this.getPlayerOrThrow().addAttachment(this.plugin));
		this.collections.add(col);
		return col;
		
	}
	
	@Override
	public void removeCollection(PermissionCollection col) {
		
		if (col instanceof AttachmentWrapperCollection) {
			
			this.collections.remove(col);
			this.getPlayerOrThrow().removeAttachment(((AttachmentWrapperCollection) col).getAttachment());
			
		} else {
			NeocoreAPI.getLogger().warning("Um, someone tried to remove an attachment from player " + this.getUniqueId() + ", but it doesn't seem like they passed in an AttachmentWrapperCollection that we like.");
		}
		
	}

	@Override
	public List<PermissionCollection> getCollections() {
		return new ArrayList<>(this.collections);
	}

	@Override
	public void applyChanges() {
		this.getPlayerOrThrow().recalculatePermissions();
	}
	
}
