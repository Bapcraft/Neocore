package io.neocore.api.database;

public class AbstractPersistentRecord implements Persistent {
	
	// Involved in network stuffs.
	protected transient volatile boolean dirty, invalid;
	
	private Runnable flushCallback = () -> {};
	
	@Override
	public void setDirty(boolean val) {
		this.dirty = val;
	}
	
	@Override
	public boolean isDirty() {
		return this.dirty;
	}
	
	@Override
	public void invalidate() {
		this.invalid = true;
	}
	
	@Override
	public boolean isGloballyValid() {
		return !this.invalid;
	}
	
	@Override
	public void setFlushProcedure(Runnable callback) {
		this.flushCallback = callback;
	}
	
	@Override
	public Runnable getFlushProcedure() {
		return this.flushCallback;
	}
	
}
