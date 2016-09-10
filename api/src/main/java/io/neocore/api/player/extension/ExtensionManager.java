package io.neocore.api.player.extension;

import java.util.Collections;
import java.util.List;

/**
 * Where all extensions must be registered before any player data is loaded.
 * 
 * @author treyzania
 */
public class ExtensionManager {

	private List<RegisteredExtension> extensions;
	
	public ExtensionManager() {
		
	}
	
	/**
	 * Registeres the extension class as a type of player extension.
	 * 
	 * @param clazz The class to register.
	 * @return The registration entry.
	 */
	public RegisteredExtension registerExtension(Class<? extends Extension> clazz) {
		
		ExtensionType anno = clazz.getAnnotation(ExtensionType.class);
		if (anno == null) throw new NullPointerException("Extension " + clazz.getName() + " doesn't have @ExtensionType on it!");
		
		// After verification we can just pull out the information.
		String name = anno.name();
		Class<? extends ExtensionBuilder> deser = anno.builder();
		
		// Then reassemble it.
		RegisteredExtension reg = new RegisteredExtension(name, clazz, deser);
		this.extensions.add(reg);
		
		return reg;
		
	}
	
	/**
	 * Finds the registration entry using the given simple name.
	 * 
	 * @param name The name of extension to look for.
	 * @return The registration entry.
	 */
	public RegisteredExtension getType(String name) {
		
		for (RegisteredExtension reg : this.extensions) {
			if (reg.getName().equals(name)) return reg;
		}
		
		return null;
		
	}
	
	/**
	 * Deserializes the serialized extension into an actual Extension object.
	 * 
	 * @param name The name of the extension.
	 * @param data The serialized extension.
	 * @return The deserialized extension.
	 */
	public Extension deserialize(String name, String data) {
		
		RegisteredExtension reg = this.getType(name);
		return reg != null ? reg.deserialize(data) : new UnknownExtension(name, data);
		
	}
	
	public List<RegisteredExtension> getTypes() {
		return Collections.unmodifiableList(this.extensions);
	}
	
}
