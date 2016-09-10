package io.neocore.bukkit.cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import io.neocore.api.cmd.AbstractCommand;
import io.neocore.api.cmd.ErrorSignal;
import io.neocore.api.cmd.InsufficientPermissionsSignal;
import io.neocore.api.cmd.InvalidUsageSignal;
import io.neocore.api.cmd.SuccessSignal;

public class CommandWrapper implements CommandExecutor {
	
	private AbstractCommand command;
	
	public CommandWrapper(AbstractCommand cmd) {
		this.command = cmd;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		CommandSenderWrapper wrapper = new CommandSenderWrapper(sender);
		
		try {
			
			// This throws things based on the resolution.
			this.command.onExecute(wrapper, args);
			
		} catch (SuccessSignal s) {
			return true;
		} catch (ErrorSignal s) {
			return true;
		} catch (InsufficientPermissionsSignal s) {
			return true;
		} catch (InvalidUsageSignal s) {
			return false;
		}
		
		throw new IllegalStateException("Command finished execution but no resolution was announced!");
		
	}

}
