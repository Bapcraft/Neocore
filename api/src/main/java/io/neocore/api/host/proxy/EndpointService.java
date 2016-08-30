package io.neocore.api.host.proxy;

public interface EndpointService extends NetworkParticipant {
	
	/**
	 * @return A network endpoint representing <i>this</i> server.
	 */
	public NetworkEndpoint getServerEndpoint();
	
	public default String getServerNetworkName() {
		return this.getServerEndpoint().getName();
	}
	
}
