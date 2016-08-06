package io.neocore.api.task;

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
