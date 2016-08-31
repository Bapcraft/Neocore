package io.neocore.api.host.proxy;

/**
 * Receives events regarding proxy traffic.
 * 
 * @author treyzania
 */
public interface ProxyAcceptor {
	
	/**
	 * Should be called when a player is moving between two endpoints.
	 * 
	 * @param event The event.
	 */
	public void onDownstreamTransfer(DownstreamTransferEvent event);
	
}
