package io.neocore.api.player.extension;

/**
 * A generic data class that can be attached onto player data to be cleanly
 * stored in the database for later access.
 * 
 * @author treyzania
 */
public abstract class Extension {

	/**
	 * Gets the name of the extension based on the <code>@ExtensionType</code>
	 * annotation.
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
	 * Checks to see if this object needs to be updated into the database.
	 * 
	 * @return <code>true</code> if the object is pending a flush,
	 *         <code>false</code> otherwise.
	 */
	public abstract boolean isDirty();

	/**
	 * Sets the object as having been flushed to the database, therefore no
	 * longer to be considered "dirty".
	 */
	public abstract void clean();

}
