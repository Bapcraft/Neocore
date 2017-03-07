package io.neocore.common.tasks;

import io.neocore.api.task.Task;
import io.neocore.api.task.TaskDelegator;

public class NeocoreTaskDelegator implements TaskDelegator {

	@Override
	public String getName() {
		return "Neocore";
	}

	@Override
	public boolean recoverFromProblem(Task task, Throwable problem) {

		// FIXME
		return false;

	}

}
