package io.neocore.api.player.extension;

import java.util.Map;
import java.util.Set;

/**
 * Where all extensions must be registered before any player data is loaded.
 * 
 * FIXME Too much of this is all static.
 * 
 * @author treyzania
 */
public class ExtensionRegistry {

	private Map<Class<? extends Extension>, String> mapping;
	
	private static ExtensionRegistry instance;
	
	/**
	 * Verifies the sanity of 
	 * 
	 * @param extClass The class of the extension to be registerd.
	 */
	public static void registerExtension(Class<? extends Extension> extClass) {
		
		ExtensionType ext = extClass.getAnnotation(ExtensionType.class);
		if (ext == null) throw new IllegalArgumentException("The class " + extClass + " does not have an @ExtensionType annotation on it!");
		
		instance.mapping.put(extClass, ext.value());
		
	}
	
	public static String getExtensionName(Class<? extends Extension> ext) {
		return instance.mapping.get(ext);
	}
	
	public static Class<? extends Extension> getExtensionClass(String name) {
		
		Set<Class<? extends Extension>> keys = instance.mapping.keySet();
		
		for (Class<? extends Extension> k : keys) {
			if (instance.mapping.get(k).equals(name)) return k; 
		}
		
		return null;
		
	}
	
}
