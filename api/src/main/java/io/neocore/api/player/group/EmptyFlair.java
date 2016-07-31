package io.neocore.api.player.group;

import net.md_5.bungee.api.ChatColor;

/**
 * A simple, empty flair class that effectively leaves the player's name unchanged.
 * 
 * @author treyzania
 */
public class EmptyFlair extends AbstractFlair {
	
	private String name;
	
	public EmptyFlair(String name) {
		this.name = name;
	}
	
	public EmptyFlair() {
		this("");
	}
	
	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public ChatColor getPrimaryColor() {
		return ChatColor.RESET;
	}
	
}
