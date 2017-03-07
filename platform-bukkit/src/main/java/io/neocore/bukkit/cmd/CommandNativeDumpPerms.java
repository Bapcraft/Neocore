package io.neocore.bukkit.cmd;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import io.neocore.api.cmd.AbstractCommand;
import io.neocore.api.cmd.CmdSender;
import net.md_5.bungee.api.ChatColor;

public class CommandNativeDumpPerms extends AbstractCommand {

	public CommandNativeDumpPerms() {
		super("nativedumpperms");
	}

	@Override
	public void onExecute(CmdSender sender, String[] args) {

		Player p = Bukkit.getPlayer(sender.getUniqueId());

		if (p != null) {

			Set<PermissionAttachmentInfo> pais = p.getEffectivePermissions();

			sender.sendMessage("Permissions (" + pais.size() + "):");
			for (PermissionAttachmentInfo pai : pais) {

				sender.sendMessage("   - " + (pai.getValue() ? ChatColor.GREEN : ChatColor.RED) + pai.getPermission());

			}

		}

	}

}
