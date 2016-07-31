package io.neocore.api.player.group;

import net.md_5.bungee.api.ChatColor;

public abstract class AbstractFlair {
	
	public abstract String getName();
	
	public abstract ChatColor getPrimaryColor();
	public ChatColor getSecondaryColor() {
		return this.getPrimaryColor();
	}
	
	public ChatColor getUserColor() {
		return ChatColor.RESET;
	}
	
	public String getPrefix() {
		return "";
	}
	
	public String getSuffix() {
		return "";
	}
	
	/**
	 * Applies the specified formatting, replacing the following tokens with
	 * the appropriate values: <code>{primary}</code>,
	 * <code>{secondary}</code>, <code>{flairname}</code>,
	 * <code>{prefix}</code>, <code>{suffix}</code>, and
	 * <code>{user}</code>.
	 * 
	 * @param formatting The generic formatting scheme that has some of the above tokens
	 * @param username The literal name of the user being injected
	 * @return The formatting string with this flair applied to it.
	 */
	public String apply(String formatting, String username) {
		
		return formatting
			.replace("{flairname}", this.getName())
			.replace("{primary}", this.getPrimaryColor() + "")
			.replace("{secondary}", this.getSecondaryColor() + "")
			.replace("{prefix}", this.getPrefix())
			.replace("{suffix}", this.getSuffix())
			.replace("{user}", this.getUserColor() + username + ChatColor.RESET)
			;
		
	}
	
}
