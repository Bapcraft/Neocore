package io.neocore.api.player.extension;

/**
 * A generic extension class used when the proper class used for deserialization
 * cannot be found, and so it is in "comparability mode".
 * 
 * @author treyzania
 */
public class UnknownExtension extends Extension {

	private final String name, data;

	public UnknownExtension(String name, String data) {

		this.name = name;
		this.data = data;

	}

	@Override
	public String getName() {
		return this.name;
	}

	public String getData() {
		return this.data;
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public void clean() {
		// lol wat
	}

}
