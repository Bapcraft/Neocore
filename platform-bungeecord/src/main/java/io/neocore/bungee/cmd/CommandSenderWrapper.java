package io.neocore.bungee.cmd;

import java.util.UUID;

import io.neocore.api.cmd.CmdSender;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class CommandSenderWrapper implements CmdSender {
	
	private CommandSender sender;
	
	public CommandSenderWrapper(CommandSender sender) {
		this.sender = sender;
	}
	
	@Override
	public UUID getUniqueId() {
		
		if (this.sender instanceof ProxiedPlayer) {
			return ((ProxiedPlayer) sender).getUniqueId();
		} else {
			return new UUID(0L, 0L);
		}
		
	}
	
	@Override
	public boolean isPlayer() {
		return this.sender instanceof ProxiedPlayer;
	}
	
	@Override
	public String getUsername() {
		return this.sender.getName();
	}
	
	@Override
	public boolean isOp() {
		return !(this.sender instanceof ProxiedPlayer);
	}
	
	@Override
	public boolean hasPermission(String perm) {
		return this.sender.hasPermission(perm);
	}
	
	@Override
	public void sendMessage(String str) {
		this.sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', str)));
	}
	
}
