package io.neocore.api.player.group;

public interface Flair {
	
	/**
	 * Sets the new prefix for the flair, to be put before the player's name.
	 * 
	 * @param prefix The flair prefix.
	 */
	public void setPrefix(String prefix);
	
	/**
	 * @return The prefix put before the player's name.
	 */
	public String getPrefix();
	
	/**
	 * Sets the new suffix for the flair, to be put after the player's name.
	 * 
	 * @param suffix The flair suffix.
	 */
	public void setSuffix(String suffix);
	
	/**
	 * @return The suffix put after the player's name.
	 */
	public String getSuffix();
	
	/**
	 * Compiles the parameters of the flair for the given player's name.
	 * 
	 * @param playerName The player's name.
	 * @return The flaired name.
	 */
	public default String apply(String playerName) {
		return this.getPrefix() + playerName + this.getSuffix();
	}
	
}
