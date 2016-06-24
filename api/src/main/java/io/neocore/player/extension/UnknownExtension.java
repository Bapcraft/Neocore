package io.neocore.player.extension;

/**
 * A generic extension class used when the proper class used for
 * deserialization cannot be found, and so it is in "comparability mode". 
 * 
 * @author treyzania
 */
public class UnknownExtension extends Extension {

	private final String name;
	
	public UnknownExtension(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return this.name;
	}
	
}
