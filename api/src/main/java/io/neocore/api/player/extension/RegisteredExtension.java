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
	 * @return The plaintext name of this registration
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * @return The extension's class
	 */
	public Class<? extends Extension> getExtensionClass() {
		return this.extensionClass;
	}
	
	/**
	 * @return The extension builder's class
	 */
	public Class<? extends ExtensionBuilder> getBuilderClass() {
		return this.builderClass;
	}
	
	/**
	 * Deserialized the extension into an actual object from the serialization provided.
	 * 
	 * @param data The serialized extension
	 * @return The actual extension object
	 */
	public Extension deserialize(String data) {
		return this.getBuilder().deserialize(data, this.extensionClass);
	}
	
	/**
	 * @return A new builder of the type associated with the Extension
	 */
	public ExtensionBuilder getBuilder() {
		
		try {
			return this.builderClass.newInstance();
		} catch (InstantiationException e) {
			throw new IllegalArgumentException("Bad builder!", e);
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException("Bad builder!", e);
		}
		
	}
	
	/**
	 * Serialized the extension into a String.
	 * 
	 * @param ext The extension to serialize
	 * @return The serialized extension
	 */
	public String serialize(Extension ext) {
		
		ExtensionBuilder builder = this.getBuilder();
		
		if (builder.isCompatible(ext)) {
			return builder.serialize(ext);
		} else {
			throw new IllegalArgumentException("bitch");
		}
		
	}
	
	@Override
	public int compareTo(RegisteredExtension o) {
		return this.name.compareTo(o.name);
	}
	
}
