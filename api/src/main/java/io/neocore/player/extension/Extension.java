package io.neocore.player.extension;

/**
 * A generic data class that can be attached onto player data to be seamlessly
 * stored in the database for later access.
 * 
 * @author treyzania
 */
public abstract class Extension {
	
	public String getName() {
		
		// Very simple check to pull it from this class's own annotation.
		Class<? extends Extension> clazz = this.getClass();
		ExtensionType ext = clazz.getAnnotation(ExtensionType.class);
		
		return ext != null ? ext.value() : null;
		
	}
	
}
