package io.neocore.api.host.chat;

import io.neocore.api.host.HostServiceProvider;
import io.neocore.api.player.IdentityProvider;
import io.neocore.api.player.PlayerIdentity;

public interface ChatService extends HostServiceProvider, IdentityProvider<ChattablePlayer> {

	/**
	 * Called when a chat acceptor should be registered.
	 * 
	 * @param acceptor
	 *            The acceptor.
	 */
	public void setChatAcceptor(ChatAcceptor acceptor);

	/**
	 * @return The acceptor currently in use.
	 */
	public ChatAcceptor getChatAcceptor();

	/**
	 * Spoofs a message sent from the specified player.
	 * 
	 * @param sender
	 *            The sender to spoof.
	 * @param message
	 *            The message to be sent.
	 */
	public void sendMessageFrom(ChattablePlayer sender, String message);

	@Override
	default Class<? extends PlayerIdentity> getIdentityClass() {
		return ChattablePlayer.class;
	}

}
