package io.neocore.common.cmd;

import java.util.List;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.cmd.AbstractCommand;
import io.neocore.api.cmd.CmdSender;
import io.neocore.api.host.Context;
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
		if (args.length != 1 && args.length != 2) this.badUsage();
		
		String groupName = args[0];
		Context context = Context.create(args.length == 2 ? !args[1].equals("null") ? args[1] : null : null);
		
		PermissionManager man = NeocoreAPI.getAgent().getPermissionManager();
		Group g = man.getGroup(groupName);
		
		if (g != null) {
			
			sender.sendMessage("Group: " + g.getDisplayName() + " &7(" + g.getName() + ")");
			
			List<PermissionEntry> perms = g.getPermissions();
			for (PermissionEntry perm : perms) {
				
				if (perm.getContext() == context) {
					sender.sendMessage(" - " + (perm.isSet() ? (perm.getState() ? "&a" : "&c") : "&e") + perm.getPermissionNode());
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
