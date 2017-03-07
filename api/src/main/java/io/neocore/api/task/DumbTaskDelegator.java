package io.neocore.api.task;

/**
 * A task delegator that only has a name, without any handing for errors.
 * 
 * @author treyzania
 */
public class DumbTaskDelegator implements TaskDelegator {

	private String name;

	public DumbTaskDelegator(String name) {
		this.name = name;
	}

	public DumbTaskDelegator() {
		this("UNDEFINED");
	}

	@Override
	public String getName() {
		return this.name;
	}

}
