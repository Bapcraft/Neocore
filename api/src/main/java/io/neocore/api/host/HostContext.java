package io.neocore.api.host;

/**
 * Represents something generic for identifying the host in a human-readable way.
 * 
 * @author treyzania
 */
public abstract class HostContext implements Context {
	
	private String name;
	
	public HostContext(String name) {
		this.name = name;
	}
	
	@Override
	public String getName() {
		return this.name;
	}
	
	/**
	 * @return The fully qualified name of this server, including the potential name of the upstream proxy.
	 */
	public abstract String getFullName();
	
	@Override
	public int hashCode() {
		
		// Yucky auto-generated code.
		
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
		
	}
	
	@Override
	public boolean equals(Object obj) {
		
		// Yucky auto-generated code.
		
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		HostContext other = (HostContext) obj;
		if (name == null) {
			if (other.name != null) return false;
		} else if (!name.equals(other.name)) return false;
		return true;
		
	}
	
}
