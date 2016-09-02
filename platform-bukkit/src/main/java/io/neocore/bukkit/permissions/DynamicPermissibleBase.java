package io.neocore.bukkit.permissions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.permissions.ServerOperator;

import io.neocore.api.player.NeoPlayer;
import io.neocore.api.player.permission.DynamicPermissionCollection;

// TODO FIXME Dynamic permission don't follow proper rules regarding permission trees.  It's entirely hit-or-miss.
public class DynamicPermissibleBase extends PermissibleBase {
	
	private List<DynamicPermissionCollection> dynamicAttachments;
	private NeoPlayer player;
	
	public DynamicPermissibleBase(ServerOperator opable, NeoPlayer player) {
		
		super(opable);
		
		this.dynamicAttachments = new ArrayList<>();
		this.player = player;
		
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
	
	public io.neocore.api.player.permission.DynPerm getDynamicPerm(String name) {
		
		for (DynamicPermissionCollection dyn : this.dynamicAttachments) {
			
			io.neocore.api.player.permission.DynPerm perm = dyn.getPerm(name);
			if (perm != null) return perm;
			
		}
		
		return null;
		
	}
	
	@Override
	public boolean hasPermission(String inName) {
		
		if (this.isSetDynamically(inName)) {
			
			io.neocore.api.player.permission.DynPerm perm = this.getDynamicPerm(inName);
			return perm.getState(this.player);
			
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
		// TODO Auto-generated method stub
		return super.getEffectivePermissions();
	}
	
}
