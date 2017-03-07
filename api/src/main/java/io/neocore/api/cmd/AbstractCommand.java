package io.neocore.api.cmd;

import java.util.Collections;
import java.util.List;

public abstract class AbstractCommand {

	private String name;

	public AbstractCommand(String name) {
		this.name = name;
	}

	public abstract void onExecute(CmdSender sender, String[] args);

	public String getName() {
		return this.name;
	}

	public String getUsage() {
		return "";
	}

	public String getDescription() {
		return "";
	}

	public String getPrefix() {
		return "neocore";
	}

	public List<String> getAliases() {
		return Collections.emptyList();
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

	public boolean isNativeOverride() {
		return false;
	}

	public String getEndpointName() {
		return this.getName();
	}

	public String getProxyName() {
		return "!" + this.getName();
	}

}
