package io.neocore.api.player.extension;

/**
 * A generic data class that can be attached onto player data to be seamlessly
 * stored in the database for later access.
 * 
 * @author treyzania
 */
public abstract class Extension {
	
	/**
	 * Gets the name of the extension based on the <code>@ExtensionType</code> annotation.
	 * 
	 * @return The extension name.
	 */
	public String getName() {
		
		// Very simple check to pull it from this class's own annotation.
		Class<? extends Extension> clazz = this.getClass();
		ExtensionType ext = clazz.getAnnotation(ExtensionType.class);
		
		return ext != null ? ext.name() : null;
		
	}
	
	/**
	 * Serialized this extension object into a String that can be usedd by the database.
	 * 
	 * @return The serialized extension.
	 */
	public abstract String serialize();
	
}
