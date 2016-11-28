package io.neocore.common.cmd;

import io.neocore.api.cmd.AbstractCommand;
import io.neocore.api.cmd.CmdSender;

public class CommandCheckPerm extends AbstractCommand {
	
	public CommandCheckPerm() {
		super("checkperm");
	}
	
	@Override
	public void onExecute(CmdSender sender, String[] args) {
		
		if (!sender.hasPermission("neocore.cmd.checkperm")) this.noPerms();
		if (args.length != 1) this.badUsage();
		
		String permName = args[0].toLowerCase();
		sender.sendMessage("Check: " + permName + " - " + sender.hasPermission(permName));
		
	}
	
}
