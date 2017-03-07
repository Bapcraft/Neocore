package io.neocore.api.database;

public interface Persistent {

	/**
	 * Sets the dirtiness state of the object.
	 * 
	 * @param val
	 *            The dirtiness.
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

	/**
	 * Sets the callback to invoke then this object should be flushed.
	 * 
	 * @param callback
	 *            The callback.
	 */
	public void setFlushProcedure(Runnable callback);

	/**
	 * @return The callback that will be invoked when flushed.
	 */
	public Runnable getFlushProcedure();

	/**
	 * Invokes the callback to flush the object to the database.
	 */
	public default void flush() {
		if (this.isDirty())
			this.getFlushProcedure().run();
	}

}
