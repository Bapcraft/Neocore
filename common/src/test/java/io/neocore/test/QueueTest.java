package io.neocore.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.junit.Test;

import io.neocore.api.task.DumbTaskDelegator;
import io.neocore.api.task.RunnableTask;
import io.neocore.api.task.TaskQueue;
import io.neocore.common.tasks.Worker;

public class QueueTest {

	@Test
	public void testQueueProcessing() {

		TaskQueue q = new TaskQueue();
		Worker w = new Worker(q, Logger.getAnonymousLogger());
		DumbTaskDelegator d = new DumbTaskDelegator("foo");

		List<String> list = new ArrayList<>();
		q.enqueue(new RunnableTask(d, () -> list.add("a")));
		q.enqueue(new RunnableTask(d, () -> list.add("b")));
		q.enqueue(new RunnableTask(d, () -> list.add("c")));
		q.enqueue(new RunnableTask(d, () -> w.stop()));
		w.run();

		List<String> expected = new ArrayList<>(Arrays.asList("a", "b", "c"));
		assertEquals(expected.size(), list.size());
		assertEquals(expected.get(0), list.get(0));
		assertEquals(expected.get(1), list.get(1));
		assertEquals(expected.get(2), list.get(2));

	}

}
