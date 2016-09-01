package io.neocore.api.player.group;

import net.md_5.bungee.api.ChatColor;

/**
 * A basic flair with a name, one color, prefix, and suffix.
 * 
 * @author treyzania
 */
public class SimpleFlair extends AbstractFlair {
	
	private String name;
	private ChatColor color;
	private String prefix, suffix;
	
	public SimpleFlair(String name, ChatColor color, String prefix, String suffix) {
		
		this.name = name;
		this.color = color;
		
		this.prefix = prefix;
		this.suffix = suffix;
		
	}
	
	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public ChatColor getPrimaryColor() {
		return this.color;
	}
	
	@Override
	public String getPrefix() {
		return this.prefix;
	}
	
	@Override
	public String getSuffix() {
		return this.suffix;
	}
	
}
