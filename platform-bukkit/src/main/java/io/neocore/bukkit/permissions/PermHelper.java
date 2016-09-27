package io.neocore.bukkit.permissions;

import java.util.Locale;

public class PermHelper {
	
	private static final String PERM_SEP_REGEX = "\\.";
	private static final String WILDCARD = "*";
	
	public static boolean matches(String base, String query) {
		
		base = base.toLowerCase(Locale.ENGLISH);
		query = query.toLowerCase(Locale.ENGLISH);
		
		// Check if it matches exactly.
		if (base.equals(query)) return true;
		
		String[] baseParts = base.split(PERM_SEP_REGEX);
		String[] queryParts = query.split(PERM_SEP_REGEX);
		
		// We can't match if the match is higher up than the base.
		if (queryParts.length < baseParts.length) return false;
		
		int baseMax = baseParts.length - 1;
		
		for (int i = 0; i <= baseMax; i++) {
			
			String bp = baseParts[i];
			String qp = queryParts[i];

			// `some.perm.node` against `some.perm.*`
			if (bp.equals(WILDCARD)) return true;
			
			// `some.[perm].node` against `some.[perm].node`
			if (!bp.equals(qp)) return false;
			
		}
		
		return false;
		
	}
	
}
