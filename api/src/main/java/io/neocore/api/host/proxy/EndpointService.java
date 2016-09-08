package io.neocore.api.host.proxy;

public interface EndpointService extends NetMemberProvider {
	
	/**
	 * @return A network endpoint representing <i>this</i> server.
	 */
	public NetworkEndpoint getServerEndpoint();
	
	/**
	 * @return The network name of this server.
	 */
	public default String getServerNetworkName() {
		return this.getServerEndpoint().getName();
	}
	
}
