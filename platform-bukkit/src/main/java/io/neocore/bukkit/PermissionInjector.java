package io.neocore.bukkit;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissibleBase;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.player.NeoPlayer;
import io.neocore.api.player.permission.DynamicPermissionCollection;
import io.neocore.bukkit.permissions.DynamicPermissibleBase;
import io.neocore.common.HostPlayerInjector;

public class BukkitPlayerInjector implements HostPlayerInjector {
	
	private static boolean inited = false;
	
	private static Field craftHumanEntityDotPerm;
	private static List<Field> permissibleBaseFields = new ArrayList<>();
	
	@Override
	public Supplier<DynamicPermissionCollection> injectPermissions(NeoPlayer player) {
		
		init();
		
		Player p = Bukkit.getPlayer(player.getUniqueId());
		
		Supplier<DynamicPermissionCollection> supplier = null;
		
		// Bukkit internals bullshit
		try {
			
			PermissibleBase oldPb = (PermissibleBase) craftHumanEntityDotPerm.get(p);
			DynamicPermissibleBase newPb = new DynamicPermissibleBase(p, player);
			
			for (Field f : permissibleBaseFields) {
				
				// Really quick and dirty copy of the values in the old PermissibleBase.
				f.set(newPb, f.get(oldPb));
				
			}
			
			craftHumanEntityDotPerm.set(p, newPb);
			supplier = () -> newPb.getNewDynamicPermissionCollection();
			
		} catch (Exception e) {
			NeocoreAPI.getLogger().log(Level.SEVERE, "Error injecting dynamic permissions!", e);
		}
		
		return supplier;
		
	}
	
	private static void init() {
		
		if (inited) return;
		
		// TODO This whole `try` block is terrible.  Replace this system that doesn't risk being broken in a future update.
		try {
			
			Class<?> craftHumanEntity = NMSHelper.formatNmsClass("entity.CraftHumanEntity");
			Field[] fields = craftHumanEntity.getDeclaredFields();
			
			for (Field f : fields) {
				
				int modifiers = f.getModifiers();
				
				if (f.getType().equals(PermissibleBase.class) && Modifier.isProtected(modifiers) && Modifier.isFinal(modifiers)) {
					
					craftHumanEntityDotPerm = f;
					break;
					
				}
				
			}
			
			// We'll be using this a lot, so we can just leave it open.
			craftHumanEntityDotPerm.setAccessible(true);
			
		} catch (ClassNotFoundException e) {
			NeocoreAPI.getLogger().log(Level.SEVERE, "Error initializing host injector class!", e);
		}
		
		// Now apply fields from PermissibleBase to copy over.
		permissibleBaseFields.addAll(Arrays.asList(PermissibleBase.class.getDeclaredFields()));
		
		// Same for all of these...
		permissibleBaseFields.forEach(f -> f.setAccessible(true));
		
		inited = true;
		
	}
	
}
