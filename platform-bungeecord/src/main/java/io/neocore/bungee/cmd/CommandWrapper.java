package io.neocore.bungee.cmd;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.cmd.AbstractCommand;
import io.neocore.api.cmd.ErrorSignal;
import io.neocore.api.cmd.InsufficientPermissionsSignal;
import io.neocore.api.cmd.InvalidUsageSignal;
import io.neocore.api.cmd.SuccessSignal;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class CommandWrapper extends Command {

	private AbstractCommand command;
	
	public CommandWrapper(AbstractCommand cmd) {
		
		super("!" + cmd.getName());
		
		this.command = cmd;
		
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		
		CommandSenderWrapper wrapper = new CommandSenderWrapper(sender);
		
		try {
			
			// This throws things based on the resolution.
			this.command.onExecute(wrapper, args);
			
		} catch (SuccessSignal s) {
			// idk
		} catch (ErrorSignal s) {
			// idk
		} catch (InsufficientPermissionsSignal s) {
			sender.sendMessage(new TextComponent(ChatColor.RED + "Insufficient permissions."));
		} catch (InvalidUsageSignal s) {
			sender.sendMessage(new TextComponent(ChatColor.RED + "Insufficient permissions."));
		}
		
		NeocoreAPI.getLogger().warning("Command " + this.getName() + " finished execution but no explicit resolution was announced!");
		
	}

}
