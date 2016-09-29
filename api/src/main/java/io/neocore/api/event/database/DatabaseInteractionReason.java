package io.neocore.api.event.database;

public interface DatabaseInteractionReason {
	
	public String getAs();
	
	public default String getFullName() {
		return this.getClass().getSimpleName().toUpperCase().replaceAll("REASON", ":").concat(this.getAs());
	}
	
}
