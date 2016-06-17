package io.neocore.player.extension;

import java.util.Map;
import java.util.Set;

/**
 * Where all extensions must be registered before any player data is loaded.
 * 
 * @author treyzania
 */
public class ExtensionRegistry {

	private Map<Class<? extends PlayerExtension>, String> mapping;
	
	private static ExtensionRegistry instance;
	
	/**
	 * Verifies the sanity of 
	 * 
	 * @param extClass The class of the extension to be registerd.
	 */
	public static void registerExtension(Class<? extends PlayerExtension> extClass) {
		
		Extension ext = extClass.getAnnotation(Extension.class);
		if (ext == null) throw new IllegalArgumentException("The class " + extClass + " does not have an @Extension annotation on it!");
		
		instance.mapping.put(extClass, ext.name());
		
	}
	
	public static String getExtensionName(Class<? extends PlayerExtension> ext) {
		return instance.mapping.get(ext);
	}
	
	public static Class<? extends PlayerExtension> getExtensionClass(String name) {
		
		Set<Class<? extends PlayerExtension>> keys = instance.mapping.keySet();
		
		for (Class<? extends PlayerExtension> k : keys) {
			if (instance.mapping.get(k).equals(name)) return k; 
		}
		
		return null;
		
	}
	
}
