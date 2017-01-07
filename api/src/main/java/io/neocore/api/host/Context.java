package io.neocore.api.host;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents human-readable properties about the context of the host.  A host can have multiple simultaneous contexts.
 * 
 * @author treyzania
 */
public interface Context {
	
	public static final Context GLOBAL = new LesserContext("GLOBAL");
	static final List<Context> createdContexts = new ArrayList<>(Arrays.asList(GLOBAL)); // Can't define initalizers so this is hard.
	
	/**
	 * @return The human-readable name of the context.
	 */
	public String getName();
	
	/**
	 * Creates a context with the given name, potentially pulling it from the
	 * list of already-created contexts.  If a <code>null</code> is passed,
	 * then the GLOBAL context is returned.
	 * 
	 * @param name The name of the context
	 * @return The context
	 */
	public static Context create(String name) {
		
		if (name == null) return GLOBAL;
		
		for (Context ctx : createdContexts) {
			if (ctx != null && ctx.getName() != null && ctx.getName().equals(name)) return ctx;
		}
		
		Context added = new LesserContext(name);
		createdContexts.add(added);
		
		return added;
		
	}
	
	/**
	 * Checks if the permission context specified can fit into the host or
	 * "environment" context specified.
	 * 
	 * @param env The "environment"/host context
	 * @param perm The permission context
	 * @return If the permission should be active
	 */
	public static boolean checkCompatility(Context env, Context perm) {
		
		if (env == null || perm == null) return false;
		if (perm == GLOBAL || env == GLOBAL) return true;
		
		return env == perm;
		
	}
	
}
