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
	
	@Override
	public int hashCode() {
		
		// Yucky auto-generated code!
		
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
		
	}
	
	@Override
	public boolean equals(Object obj) {
		
		// Yucky auto-generated code!
		
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LesserContext other = (LesserContext) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
		
	}
	
}
