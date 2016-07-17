package io.neocore.api.player.group;

/**
 * A simple, empty flair class that effectively leaves the player's name unchanged.
 * 
 * @author treyzania
 */
public class EmptyFlair implements Flair {

	@Override
	public String getPrefix() {
		return "";
	}

	@Override
	public String getSuffix() {
		return "";
	}

}
