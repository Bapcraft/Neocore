package io.neocore.common.cmd;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.ServiceManager;
import io.neocore.api.cmd.AbstractServiceCommand;
import io.neocore.api.cmd.CmdSender;
import io.neocore.api.database.group.GroupService;
import io.neocore.api.player.group.Group;
import io.neocore.api.player.permission.PermissionManager;

public class CommandCreateGroup extends AbstractServiceCommand {

	public CommandCreateGroup(ServiceManager serv) {
		super("creategroup", serv);
	}

	@Override
	public void onExecute(CmdSender sender, String[] args) {

		if (!sender.hasPermission("neocore.cmd.permissions.creategroup"))
			this.noPerms();
		if (args.length < 2)
			this.badUsage();

		// Extract parameters.
		String rawName = args[0];
		StringBuilder prettyName = new StringBuilder(args[1]);
		for (int i = 2; i < args.length; i++) {
			prettyName.append(" " + args[i]);
		}

		// Create, configure, and save the group.
		GroupService groups = this.getService(GroupService.class);
		Group g = groups.createGroup(rawName);
		g.setDisplayName(prettyName.toString());
		g.flush();

		// Maybe?
		PermissionManager pm = NeocoreAPI.getAgent().getPermissionManager();
		pm.repopulatePermissions();

		sender.sendMessage("Created group " + prettyName + " (" + rawName + ").");
		this.success();

	}

}
