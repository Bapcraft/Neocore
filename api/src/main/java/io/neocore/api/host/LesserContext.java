package io.neocore.api.host;

/**
 * A simple context object with only one name.
 * 
 * @author treyzania
 */
public class LesserContext implements Context {
	
	private String name;
	
	public LesserContext(String name) {
		this.name = name;
	}
	
	@Override
	public String getName() {
		return this.name;
	}
	
}
