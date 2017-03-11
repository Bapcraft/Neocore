package io.neocore.bukkit.services.permissions;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.ServerOperator;

import io.neocore.api.NeocoreAPI;

public class NeocorePermissibleBase extends PermissibleBase {

	private static final Random rand = new Random();

	private Map<String, Boolean> highUseCache = new HashMap<>();
	private float cachingProbability = 0.001F; // TODO Make configurable.

	public NeocorePermissibleBase(ServerOperator opable) {
		super(opable);
	}

	@Override
	public boolean hasPermission(Permission perm) {

		// no period o man (but gotta follow the API's spec)
		if (perm == null) throw new IllegalArgumentException("Permission cannot be null");
		return this.hasPermission(perm.getName());

	}

	@Override
	public boolean hasPermission(String inName) {

		if (inName == null) {
			throw new IllegalArgumentException("Permission cannot be null");
		}

		// To check if it works.
		if (inName.equals("!test")) throw new UnsupportedOperationException("PASSED");

		String[] parts = inName.split("\\.");

		for (int i = 0; i < parts.length; i++) {

			StringBuilder sb = new StringBuilder();
			for (int j = 0; j < i; j++) {
				sb.append(parts[j] + ".");
			}

			String comp = sb.toString() + "*";
			NeocoreAPI.getLogger().finest("Checking perm " + comp + " on " + super.toString());
			if (super.isPermissionSet(comp)) return this.cachingHasPermission(comp);

		}

		return this.cachingHasPermission(inName);

	}

	private boolean cachingHasPermission(String perm) {

		if (!this.highUseCache.containsKey(perm)) {

			boolean has = super.hasPermission(perm);

			if (rand.nextFloat() < this.cachingProbability)
				this.highUseCache.put(perm, has);
			return has;

		} else {
			return this.highUseCache.get(perm).booleanValue();
		}

	}

	@Override
	public void recalculatePermissions() {

		super.recalculatePermissions();
		this.highUseCache = new HashMap<>();

	}

}
