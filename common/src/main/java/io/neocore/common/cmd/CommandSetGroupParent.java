package io.neocore.common.cmd;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.NeocoreInstaller;
import io.neocore.api.cmd.AbstractCommand;
import io.neocore.api.cmd.CmdSender;
import io.neocore.api.database.player.DatabasePlayer;
import io.neocore.api.player.NeoPlayer;
import io.neocore.api.player.group.Group;
import io.neocore.api.player.permission.PermissionManager;

public class CommandSetGroupParent extends AbstractCommand {

	public CommandSetGroupParent() {
		super("setparent");
	}

	@Override
	public void onExecute(CmdSender sender, String[] args) {
		
		if (!sender.hasPermission("neocore.cmd.permissions.setparent")) this.noPerms();
		if (args.length != 2) this.badUsage();
		
		String groupName = args[0];
		String parentName = args[1];
		
		PermissionManager pm = NeocoreAPI.getAgent().getPermissionManager();
		Group group = pm.getGroup(groupName);
		Group parent = parentName.equals("null") ? null : pm.getGroup(parentName);
		
		if (group == null) {
			
			sender.sendMessage("Group \"" + groupName + "\" does not exist!");
			this.error();
			
		}
		
		if (parent == null && !parentName.equalsIgnoreCase("null")) {
			
			sender.sendMessage("Group \"" + parentName + "\" does not exist!");
			this.error();
			
		}
		
		// Check restriction levels.
		if (sender.isPlayer()) {
			
			NeoPlayer np = NeocoreAPI.getAgent().getPlayer(sender.getUniqueId());
			
			if (np.hasIdentity(DatabasePlayer.class)) {
				
				DatabasePlayer dbp = np.getIdentity(DatabasePlayer.class);
				
				int level = dbp.getRestrictionLevel();
				
				if (group.getRestrictionLevel() > level || (parent != null && parent.getRestrictionLevel() > level)) {
					
					sender.sendMessage("You need a higher restriction level.");
					sender.sendMessage(" - " + group.getDisplayName() + ": " + group.getRestrictionLevel());
					if (parent != null) sender.sendMessage(" - " + parent.getDisplayName() + ": " + parent.getRestrictionLevel());
					sender.sendMessage(" - You: " + level);
					this.noPerms();
					
				}
				
			} else {
				sender.sendMessage("You don't have a database identity active on this server.  Check the server's status.");
			}
			
		}
		
		// At this point we should be solid.
		group.setParent(parent);
		group.flush();
		pm.repopulatePermissions();
		if (parent != null) {
			sender.sendMessage("Set parent of " + group.getDisplayName() + " to " + parent.getDisplayName() + ".");
		} else {
			sender.sendMessage("Parent removed for " + group.getDisplayName() + ".");
		}
		
		// FIXME Coupling.
		NeocoreInstaller.installed.getNetworkManager().getNetworkSync().announcePermissionsRefresh();
		
		this.success();
		
	}

}
