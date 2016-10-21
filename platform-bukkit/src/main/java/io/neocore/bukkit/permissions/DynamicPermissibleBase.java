package io.neocore.bukkit.permissions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.Supplier;

import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.permissions.ServerOperator;

import io.neocore.api.player.NeoPlayer;
import io.neocore.api.player.permission.DynPerm;
import io.neocore.api.player.permission.DynamicPermissionCollection;

public class DynamicPermissibleBase extends PermissibleBase {
	
	private List<DynamicPermissionCollection> dynamicAttachments;
	private Supplier<NeoPlayer> playerSup;
	
	public DynamicPermissibleBase(ServerOperator opable) {
		
		super(opable);
		
		this.dynamicAttachments = new ArrayList<>();
		this.playerSup = () -> { return null; }; // yucky looking lambda
		
	}
	
	/**
	 * Sets this player object to always be this NeoPlayer.
	 * 
	 * @param p The player.
	 */
	@Deprecated
	public void setPlayer(NeoPlayer p) {
		this.setPlayer(() -> { return p; });
	}
	
	/**
	 * Sets the player supplier to an object that will supply this player object.
	 * 
	 * @param p The player supplier.
	 */
	public void setPlayer(Supplier<NeoPlayer> p) {
		this.playerSup = p;
	}
	
	/**
	 * Gets the player as we currently know it.
	 * 
	 * @return The player, apparently.
	 */
	public NeoPlayer getPlayer() {
		return this.playerSup.get();
	}
	
	public void addDynamicCollection(DynamicPermissionCollection collection) {
		this.dynamicAttachments.add(collection);
	}
	
	public DynamicPermissionCollection getNewDynamicPermissionCollection() {
		
		// Instantiate and register it, we already have it.
		DynamicPermissionCollection dpc = new DynamicPermissionCollection();
		this.addDynamicCollection(dpc);
		return dpc;
		
	}
	
	public boolean removeDynamicCollection(DynamicPermissionCollection collection) {
		
		boolean did = false;
		
		Iterator<DynamicPermissionCollection> dyns = this.dynamicAttachments.iterator();
		while (dyns.hasNext()) {
			
			if (dyns.next() == collection) {
				
				dyns.remove();
				did |= true;
				
			}
			
		}
		
		return did;
		
	}
	
	public boolean isSetDynamically(String name) {
		
		for (DynamicPermissionCollection dyn : this.dynamicAttachments) {
			if (dyn.isSet(name)) return true;
		}
		
		return false;
		
	}
	
	public DynPerm getDynamicPerm(String name) {
		
		DynPerm last = null;
		
		for (DynamicPermissionCollection dyn : this.dynamicAttachments) {
			
			DynPerm perm = dyn.getMatching(name);
			
			// If we found a matching permission node then we check to see if the depth is okay and overwrite what we've found.
			if (perm != null && (last == null || last.getDepth() < perm.getDepth())) last = perm;
			
		}
		
		return last;
		
	}
	
	@Override
	public boolean hasPermission(String inName) {
		
		if (this.isSetDynamically(inName)) {
			
			DynPerm perm = this.getDynamicPerm(inName);
			return perm.getState(this.getPlayer());
			
		}		
		
		return super.hasPermission(inName);
		
	}
	
	@Override
	public boolean hasPermission(Permission perm) {
		
		if (perm == null) throw new IllegalArgumentException("Permission cannot be null");
		return this.hasPermission(perm.getName().toLowerCase(Locale.ENGLISH));
		
	}
	
	@Override
	public Set<PermissionAttachmentInfo> getEffectivePermissions() {
		return super.getEffectivePermissions();
	}
	
}
