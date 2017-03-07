package io.neocore.common.cmd;

import java.util.List;
import java.util.Map;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.cmd.AbstractCommand;
import io.neocore.api.cmd.CmdSender;
import io.neocore.api.host.permissions.PermissedPlayer;
import io.neocore.api.host.permissions.PermissionCollection;
import io.neocore.api.player.NeoPlayer;

public class CommandDumpPerms extends AbstractCommand {

	public CommandDumpPerms() {
		super("dumpperms");
	}

	@Override
	public void onExecute(CmdSender sender, String[] args) {

		if (!sender.isPlayer()) {

			sender.sendMessage("You need to be a player to run this command.");
			this.error();

		}

		// if (!sender.hasPermission("neocore.cmd.dumpperms")) this.noPerms();

		NeoPlayer np = NeocoreAPI.getPlayer(sender.getUniqueId());

		if (np.hasIdentity(PermissedPlayer.class)) {

			PermissedPlayer pp = np.getIdentity(PermissedPlayer.class);
			if (!pp.isOnline()) {

				sender.sendMessage("&cYou don't seem to be online.  wtf?");
				this.error();

			}

			List<PermissionCollection> cols = pp.getCollections();
			sender.sendMessage("Permission Collections (" + cols.size() + "):");

			int num = 1;
			for (PermissionCollection col : pp.getCollections()) {

				report(sender, "Collection " + num++);
				StringBuilder tagSb = new StringBuilder("     * Tags:&e");
				for (String tag : col.getTags()) {
					tagSb.append(" " + tag);
				}

				report(sender, tagSb.toString());
				report(sender, "     * Permissions: " + col.getPermissionsApplied().size());

				for (Map.Entry<String, Boolean> perm : col.getPermissionsApplied().entrySet()) {
					report(sender, "  - " + (perm.getValue().booleanValue() ? "&a" : "&c") + perm.getKey());
				}

			}

		} else {
			sender.sendMessage("&rYou do not have a permission identity.  Tell an admin or someone about this!");
		}

		this.success();

	}

	private static void report(CmdSender sender, String message) {

		sender.sendMessage(message);
		NeocoreAPI.getLogger().info("sender> " + message);

	}

}
