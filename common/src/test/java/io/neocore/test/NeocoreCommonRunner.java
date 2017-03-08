package io.neocore.test;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class NeocoreCommonRunner {

	public static void main(String[] args) {

		Result res = JUnitCore.runClasses(QueueTest.class);

		for (Failure fail : res.getFailures()) {
			System.out.println(fail.toString());
		}

		System.out.println(res.wasSuccessful());

	}

}
