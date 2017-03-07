package io.neocore.common.cmd;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.NeocoreInstaller;
import io.neocore.api.cmd.AbstractCommand;
import io.neocore.api.cmd.CmdSender;
import io.neocore.api.database.player.DatabasePlayer;
import io.neocore.api.host.Context;
import io.neocore.api.player.NeoPlayer;
import io.neocore.api.player.group.Group;

public class CommandSetPermission extends AbstractCommand {

	public CommandSetPermission() {
		super("setperm");
	}

	@Override
	public void onExecute(CmdSender sender, String[] args) {

		if (!sender.hasPermission("neocore.cmd.permissions.setperm"))
			this.noPerms();
		if (args.length != 4)
			this.badUsage();

		String groupName = args[0];
		Context context = Context.create(args[1]);
		String node = args[2].toLowerCase();
		String state = args[3].toLowerCase();

		Group group = NeocoreAPI.getAgent().getPermissionManager().getGroup(groupName);
		if (group == null) {

			sender.sendMessage("Group doesn't exist.  Check capitalization?");
			this.error();

		}

		// We have to verify restriction levels if the thing running this is a
		// player.
		if (sender.isPlayer()) {

			NeoPlayer np = NeocoreAPI.getAgent().getPlayerManager().getPlayer(sender.getUniqueId());
			if (np.hasIdentity(DatabasePlayer.class)) {

				DatabasePlayer dbp = np.getIdentity(DatabasePlayer.class);
				if (dbp.getRestrictionLevel() < group.getRestrictionLevel()) {

					sender.sendMessage("You need a higher permission level.");
					this.noPerms();

				}

			} else {

				sender.sendMessage(
						"We can't verifiy your permission level due to problems that we already know about.");
				this.badUsage();

			}

		}

		// The business.
		if (state.equals("unset")) {

			group.unsetPermission(context, node);
			group.flush();

		} else {

			if (state.equals("true")) {

				group.setPermission(context, node, true);
				group.flush();

			} else if (state.equals("false")) {

				group.setPermission(context, node, false);
				group.flush();

			} else {
				this.badUsage();
			}

		}

		// Networkyness
		// FIXME Coupling.
		NeocoreInstaller.installed.getNetworkManager().getNetworkSync().announcePermissionsRefresh();

		// Let's assume we're all okay.
		this.success();

	}

}
