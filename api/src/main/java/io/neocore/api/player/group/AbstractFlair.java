package io.neocore.api.player.group;

import net.md_5.bungee.api.ChatColor;

public abstract class AbstractFlair {
	
	/**
	 * Gets the name of the flair.
	 * 
	 * @return The name.
	 */
	public abstract String getName();
	
	/**
	 * @return The primary color of the flair.
	 */
	public abstract ChatColor getPrimaryColor();
	
	/**
	 * @return The secondary color of the flair.
	 */
	public ChatColor getSecondaryColor() {
		return this.getPrimaryColor();
	}
	
	/**
	 * @return The color used to display the user's name.
	 */
	public ChatColor getUserColor() {
		return ChatColor.RESET;
	}
	
	/**
	 * @return The text put before the player's name, always.
	 */
	public String getPrefix() {
		return "";
	}
	
	/**
	 * @return The text put after the player's name, always.
	 */
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
