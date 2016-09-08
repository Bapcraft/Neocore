package io.neocore.api.cmd;

public abstract class AbstractCommand {
	
	private String name;
	
	public AbstractCommand(String name) {
		this.name = name;
	}
	
	public abstract void onExecute(CmdSender sender, String[] args);
	
	public String getName() {
		return this.name;
	}
	
	protected void success() {
		throw new SuccessSignal();
	}
	
	protected void error() {
		throw new ErrorSignal();
	} 
	
	protected void noPerms() {
		throw new InsufficientPermissionsSignal();
	}
	
	protected void badUsage() {
		throw new InvalidUsageSignal();
	}
	
}
