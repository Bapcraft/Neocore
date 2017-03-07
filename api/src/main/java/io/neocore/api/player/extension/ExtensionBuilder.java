package io.neocore.api.player.extension;

public abstract class ExtensionBuilder {

	public abstract Extension deserialize(String data, Class<? extends Extension> to);

	public abstract String serialize(Extension ext);

	/**
	 * Used to more specifically check to see if something went wrong when
	 * getting the data for the extension. Not 100% necessary to implement, but
	 * useful internally for generating more detailed log messages.
	 * 
	 * @param ext
	 *            The extension to check against
	 * @return if it is compatible or not
	 */
	public boolean isCompatible(Extension ext) {
		return true;
	}

}
