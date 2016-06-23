package io.neocore;

public interface ServiceType {
	
	public String getName();
	public Class<? extends ServiceProvider> getClassType();
	
}
