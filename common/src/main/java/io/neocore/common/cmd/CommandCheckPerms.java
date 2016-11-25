package io.neocore.common.cmd;

import java.util.List;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.cmd.AbstractCommand;
import io.neocore.api.cmd.CmdSender;
import io.neocore.api.player.group.Group;
import io.neocore.api.player.group.PermissionEntry;
import io.neocore.api.player.permission.PermissionManager;

public class CommandCheckPerms extends AbstractCommand {

	public CommandCheckPerms() {
		super("checkgroup");
	}

	@Override
	public void onExecute(CmdSender sender, String[] args) {
		
		if (!sender.hasPermission("neocore.cmd.permissions.check")) this.noPerms();
		if (args.length != 2) this.badUsage();
		
		String groupName = args[0];
		String context = !args[1].equals("null") ? args[1] : null;
		
		PermissionManager man = NeocoreAPI.getAgent().getPermissionManager();
		Group g = man.getGroup(groupName);
		
		if (g != null) {
			
			sender.sendMessage("Group perms for " + g.getDisplayName() + " (" + g.getName() + ")");
			
			List<PermissionEntry> perms = g.getPermissions();
			for (PermissionEntry perm : perms) {
				
				if ((perm.getContext() == null && context == null) || (perm.getContext() != null && perm.getContext().getName().equals(context))) {
					sender.sendMessage(" - " + (perm.isSet() ? (perm.getState() ? "Y" : "N") : "U") + " " + perm.getPermissionNode());
				}
				
			}
			
			sender.sendMessage("Priority: " + g.getPriority());
			sender.sendMessage("Restriction Level: " + g.getRestrictionLevel());
			
		} else {
			sender.sendMessage("Group not found.");
		}
		
		this.success();
		
	}

}
