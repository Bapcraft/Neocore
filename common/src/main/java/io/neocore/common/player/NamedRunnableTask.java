package io.neocore.common.player;

import io.neocore.api.task.RunnableTask;
import io.neocore.api.task.TaskDelegator;

public class NamedRunnableTask extends RunnableTask {

	private String name;

	public NamedRunnableTask(TaskDelegator tg, String name, Runnable r) {

		super(tg, r);

		this.name = name;

	}

	@Override
	public String getName() {
		return this.name;
	}

}
