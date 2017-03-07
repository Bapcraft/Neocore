package io.neocore.common;

import io.neocore.api.host.ThreadInfo;

public class JnsThreadInfo implements ThreadInfo {

	private Thread thread;

	public JnsThreadInfo() {
		this(Thread.currentThread());
	}

	public JnsThreadInfo(Thread t) {
		this.thread = t;
	}

	@Override
	public boolean isRunning() {
		return this.thread.isAlive();
	}

	@Override
	public void kill() {
		this.thread.interrupt();
	}

}
