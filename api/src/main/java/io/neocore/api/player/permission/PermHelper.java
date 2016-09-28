package io.neocore.api.player.permission;

import java.util.Locale;

public class PermHelper {
	
	private static final String PERM_SEP_REGEX = "\\.";
	private static final String WILDCARD = "*";
	
	public static int matchDepth(String base, String query) {
		
		base = base.toLowerCase(Locale.ENGLISH);
		query = query.toLowerCase(Locale.ENGLISH);
		
		String[] baseParts = base.split(PERM_SEP_REGEX);
		String[] queryParts = query.split(PERM_SEP_REGEX);
		
		// Global wildcard.
		if (base.equals(WILDCARD)) return 0;
				
		
		// Check if it matches exactly.
		if (base.equals(query)) return baseParts.length;
		
		// We can't match if the match is higher up than the base.
		if (queryParts.length < baseParts.length) return -1;
		
		for (int i = 0; i < baseParts.length; i++) {
			
			String bp = baseParts[i];
			String qp = queryParts[i];
			
			// `some.perm.node` against `some.perm.*`
			if (bp.equals(WILDCARD)) return i;
			
			// `some.[perm].node` against `some.[perm].node`
			if (!bp.equals(qp)) return -1;
			
		}
		
		return -1;
		
	}
	
	public static boolean matches(String base, String query) {
		return matchDepth(base, query) > -1;
	}
	
}
