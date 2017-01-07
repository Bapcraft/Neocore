package io.neocore.common.permissions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.database.group.GroupService;
import io.neocore.api.database.player.DatabasePlayer;
import io.neocore.api.host.Context;
import io.neocore.api.host.permissions.PermissedPlayer;
import io.neocore.api.host.permissions.PermissionCollection;
import io.neocore.api.host.permissions.PermissionsService;
import io.neocore.api.player.NeoPlayer;
import io.neocore.api.player.group.Group;
import io.neocore.api.player.group.GroupMembership;
import io.neocore.api.player.group.PermissionEntry;
import io.neocore.api.player.permission.PermissionManager;
import io.neocore.common.player.PlayerManagerWrapperImpl;
import io.neocore.common.service.ServiceManagerImpl;

public class PermissionManagerImpl implements PermissionManager {
	
	public static final String COLLECTION_GROUP_CFG_TAG = "GroupConfiguration";
	
	private PlayerManagerWrapperImpl players;
	private Set<Context> contexts;
	
	private PermissionsService permsService;
	private GroupService groupService;
	
	private List<Group> cache;
	
	public PermissionManagerImpl(PlayerManagerWrapperImpl players, ServiceManagerImpl services, List<Context> contexts) {
		
		this.players = players;
		
		services.registerRegistrationHandler(PermissionsService.class, e -> {
			this.permsService = (PermissionsService) e.getServiceProvider();
		});
		
		services.registerRegistrationHandler(GroupService.class, e -> {
			this.groupService = (GroupService) e.getServiceProvider();
		});
		
		this.contexts = new HashSet<>(contexts);
		
	}
	
	private void checkReloadGroups() {
		if (this.cache == null) this.reloadGroups();
	}
	
	public void reloadGroups() {
		
		long start = System.currentTimeMillis();
		this.cache = this.groupService.loadGroups();
		long end = System.currentTimeMillis();
		
		NeocoreAPI.getLogger().info("Loaded " + this.cache.size() + " groups in " + (end - start) + " ms.");
		
	} 
	
	@Override
	public Group createGroup(String name) {
		
		this.checkReloadGroups();
		
		Group found = this.groupService.createGroup(name);
		this.cache.add(found);
		return found;
		
	}
	
	@Override
	public boolean deleteGroup(Group group) {
		
		this.checkReloadGroups();
		
		if (this.groupService.deleteGroup(group)) {
			
			this.cache.remove(group);
			return true;
			
		}
		
		return false;
		
	}

	@Override
	public Group getGroup(String name) {
		
		this.checkReloadGroups();
		
		for (Group g : this.cache) {
			if (g.getName().equals(name)) return g;
		}
		
		return null;
		
	}

	@Override
	public List<Group> getGroups() {
		
		this.checkReloadGroups();
		return new ArrayList<>(this.cache);
		
	}
	
	@Override
	public void assignPermissions(NeoPlayer player) {
		
		this.checkReloadGroups();
		Logger log = NeocoreAPI.getLogger();
		
		if (this.permsService == null) {
			
			log.severe("Can't assign permissions if we don't have a permissions service!");
			return;
			
		}
		
		if (this.groupService == null) {
			
			log.severe("Can't assign permissions if we don't have a groups service!");
			return;
			
		}
		
		if (!player.hasIdentity(DatabasePlayer.class) || !player.hasIdentity(PermissedPlayer.class)) {
			
			log.warning("Can't assign permissions onto player that doesn't have a database and permisson identity!");
			return;
			
		}
		
		DatabasePlayer dbp = player.getIdentity(DatabasePlayer.class);
		PermissedPlayer pp = player.getIdentity(PermissedPlayer.class);
		
		// Clear the relevant permission collection.
		List<PermissionCollection> cols = pp.getCollections();
		for (PermissionCollection col : cols) {
			if (col.hasTag(COLLECTION_GROUP_CFG_TAG)) pp.removeCollection(col);
		}
		
		List<Group> totalGroups = new ArrayList<>();
		
		for (GroupMembership gm : dbp.getGroupMemberships()) {
			
			// Check for validity.
			if (!gm.isCurrentlyValid()) continue;
			Group g = gm.getGroup();
			
			if (g == null) {
				
				NeocoreAPI.getLogger().warning("Found an invalid group in a membership when processing permissions for " + player.getUniqueId() + ".");
				continue;
				
			}
			
			// Add the group and their ancestors to it.
			totalGroups.add(g);
			totalGroups.addAll(g.getAncestors());
			
		}
		
		// Sort them so that their priorities are in order.
		Collections.sort(totalGroups);
		
		/*
		 * Now we have to actually figure out how to apply the permissions to
		 * the player.  Since we ordered the list of groups before doing this,
		 * we're going to start with the group with lowest priority.  Here,
		 * we'll still be doing things in order, so if we get a state for a
		 * node *after* we've already got a setting, we'll override the value
		 * in the map.  Which is what we want because we'll be sure that the
		 * group that the assignment comes from is of a higher priority.
		 */
		Map<String, Boolean> permMap = new HashMap<>();
		for (Group g : totalGroups) {
			
			// Iterate through all of the permission nodes for the group.
			for (PermissionEntry pe : g.getPermissions()) {
				
				Context permCtx = pe.getContext();
				
				for (Context envCtx : this.contexts) {
					
					if (Context.checkCompatility(envCtx, permCtx) && pe.isSet()) {
						
						permMap.put(pe.getPermissionNode(), pe.getState());
						break;
						
					}
					
				}
				
			}
			
		}
		
		// Now actually apply it.
		PermissionCollection col = pp.createCollection();
		col.addTag(COLLECTION_GROUP_CFG_TAG);
		for (Map.Entry<String, Boolean> entry : permMap.entrySet()) {
			col.setPermission(entry.getKey(), entry.getValue());
		}
		
		// Finally update everything underneath.
		pp.applyChanges();
		
		
		
	}
	
	@Override
	public void repopulatePermissions() {
		
		long start = System.currentTimeMillis();
		
		// First, make sure the groups are fully loaded.
		this.reloadGroups();
		
		// Now, go and reassign them.
		Set<NeoPlayer> nps = this.players.getOnlinePlayers();
		for (NeoPlayer np : nps) {
			this.assignPermissions(np);
		}
		
		long end = System.currentTimeMillis();
		NeocoreAPI.getLogger().info("Took " + (end - start) + " ms to fully reload permissions for " + nps.size() + " players.");
		
	}
	
}
