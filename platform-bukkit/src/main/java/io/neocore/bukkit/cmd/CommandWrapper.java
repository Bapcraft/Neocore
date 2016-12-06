package io.neocore.bukkit.cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.cmd.AbstractCommand;
import io.neocore.api.cmd.ErrorSignal;
import io.neocore.api.cmd.InsufficientPermissionsSignal;
import io.neocore.api.cmd.InvalidUsageSignal;
import io.neocore.api.cmd.SuccessSignal;
import net.md_5.bungee.api.ChatColor;

public class CommandWrapper extends Command {
	
	private AbstractCommand command;
	
	public CommandWrapper(AbstractCommand cmd) {
		
		super(cmd.getName(), cmd.getDescription(), cmd.getUsage(), cmd.getAliases());
		
		this.command = cmd;
		
	}
	
	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {

		CommandSenderWrapper wrapper = new CommandSenderWrapper(sender);
		
		try {
			
			// This throws things based on the resolution.
			this.command.onExecute(wrapper, args);
			
		} catch (SuccessSignal s) {
			return true;
		} catch (ErrorSignal s) {
			return true;
		} catch (InsufficientPermissionsSignal s) {
			
			sender.sendMessage(ChatColor.RED + "Insufficient permissions.");
			return true;
			
		} catch (InvalidUsageSignal s) {
			
			sender.sendMessage(ChatColor.RED + "Invalid usage.");
			return false;
			
		}
		
		NeocoreAPI.getLogger().warning("Command " + this.getName() + " finished execution but no explicit resolution was announced!");
		return true;
		
	}
	
}
