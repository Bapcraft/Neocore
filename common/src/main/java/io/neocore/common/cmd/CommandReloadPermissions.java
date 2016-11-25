package io.neocore.common.cmd;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.cmd.AbstractCommand;
import io.neocore.api.cmd.CmdSender;

public class CommandReloadPermissions extends AbstractCommand {
	
	public CommandReloadPermissions() {
		super("reload");
	}
	
	@Override
	public void onExecute(CmdSender sender, String[] args) {
		
		if (!sender.hasPermission("neocore.cmd.permissions.reload")) this.noPerms();
		
		long start = System.currentTimeMillis();
		NeocoreAPI.getAgent().getPermissionManager().repopulatePermissions();
		long end = System.currentTimeMillis();
		
		sender.sendMessage("Reloaded permissions in " + (end - start) + " ms.");
		
	}
	
}
