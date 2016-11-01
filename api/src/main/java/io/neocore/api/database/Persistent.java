package io.neocore.api.database;

public interface Persistent {

	/**
	 * Sets the dirtiness state of the object.
	 * 
	 * @param val The dirtiness.
	 */
	public default void setDirty(boolean val) {
		
	}
	
	/**
	 * Checks to see if this object is not consistent with the database and
	 * needs to be flushed.
	 * 
	 * @return The status of the dirtiness.
	 */
	public boolean isDirty();
	
	/**
	 * Marks this object as needing a flush to the remote database and queues
	 * the flush in the database service.
	 */
	public default void dirty() {
		this.setDirty(true);
	}
	
	/**
	 * Checks to see if this object is valid across the database.
	 * 
	 * @return The global validity.
	 */
	public boolean isGloballyValid();
	
	/**
	 * Marks this object as invalid in the database and that it needs to be
	 * purged and reobtained somehow.
	 */
	public default void invalidate() {
		
	}
	
}
