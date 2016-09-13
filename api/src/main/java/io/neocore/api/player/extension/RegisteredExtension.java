package io.neocore.api.player.extension;

public class RegisteredExtension implements Comparable<RegisteredExtension> {
	
	private final String name;
	private final Class<? extends Extension> extensionClass;
	private final Class<? extends ExtensionBuilder> builderClass;
	
	public RegisteredExtension(String name, Class<? extends Extension> ext, Class<? extends ExtensionBuilder> builder) {
		
		this.name = name;
		
		this.extensionClass = ext;
		this.builderClass = builder;
		
	}
	
	/**
	 * @return The plaintext name of this registration.
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * @return The extension's class.
	 */
	public Class<? extends Extension> getExtensionClass() {
		return this.extensionClass;
	}
	
	/**
	 * @return The extension builder's class.
	 */
	public Class<? extends ExtensionBuilder> getBuilderClass() {
		return this.builderClass;
	}
	
	/**
	 * Deserialized the extension into an actual object from the serialization provided.
	 * 
	 * @param data The seriaized extension.
	 * @return The actual extension object.
	 */
	public Extension deserialize(String data) {
		
		try {
			return this.builderClass.newInstance().deserialize(data);
		} catch (InstantiationException e) {
			throw new IllegalArgumentException("Bad builder!", e);
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException("Bad builder!", e);
		}
		
	}
	
	/**
	 * Serialized the extension into a String.
	 * 
	 * @param ext The extension to serialize.
	 * @return The serialized extension.
	 */
	public String serialize(Extension ext) {
		
		if (ext == null || ext.getClass() != this.extensionClass) throw new IllegalArgumentException("Invalid extension: " + ext);
		
		// YAAAS.
		return ext.serialize();
		
	}
	
	@Override
	public int compareTo(RegisteredExtension o) {
		return this.name.compareTo(o.name);
	}
	
}