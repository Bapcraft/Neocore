package io.neocore.common.cmd;

import java.util.Arrays;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.cmd.AbstractCommand;
import io.neocore.api.cmd.CmdSender;

public class CommandException extends AbstractCommand {
	
	public CommandException() {
		super("exception");
	}
	
	@Override
	public void onExecute(CmdSender sender, String[] args) {
		
		if (!sender.hasPermission("neocore.cmd.exception")) this.noPerms();
		if (args.length == 0 || args.length == 1);
		
		NeocoreAPI.getLogger().warning("User " + sender.getUsername() + " triggered exception directly. " + Arrays.toString(args));
		
		if (args.length == 1) {
			
			String reason = args[0].replaceAll("_", " ");
			sender.sendMessage("Throwing exception with reason \"" + reason + "\"");
			throw new RuntimeException(reason);
			
		} else {
			
			sender.sendMessage("Throwing exception...");
			throw new RuntimeException();
			
		}
		
	}
	
}
