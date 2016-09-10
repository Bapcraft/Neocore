package io.neocore.bukkit.cmd;

import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.neocore.api.cmd.CmdSender;

public class CommandSenderWrapper implements CmdSender {
	
	private CommandSender sender;
	
	public CommandSenderWrapper(CommandSender console) {
		this.sender = console;
	}
	
	@Override
	public UUID getUniqueId() {
		
		if (this.sender instanceof Player) {
			return ((Player) this.sender).getUniqueId();
		} else {
			return new UUID(0L, 0L); // 00000000-0000-0000-0000-000000000000, essentially.
		}
		
	}
	
	@Override
	public boolean isPlayer() {
		return this.sender instanceof Player;
	}
	
	@Override
	public String getUsername() {
		return this.sender.getName();
	}
	
	@Override
	public boolean isOp() {
		return this.sender.isOp();
	}
	
	@Override
	public boolean hasPermission(String perm) {
		return this.sender.hasPermission(perm);
	}
	
	@Override
	public void sendMessage(String str) {
		this.sender.sendMessage(str);
	}
	
}
