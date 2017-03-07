package io.neocore.api.infrastructure;

public interface Networkable {

	/**
	 * Returns the name of the network this member belongs to. If this value is
	 * either <code>null</code> or an empty string, then the client is assumed
	 * to not be part of a network.
	 * 
	 * @return The network's name
	 */
	String getNetworkName();

	/**
	 * Returns <code>true</code> if this "thing" is part of a network, returns
	 * <code>false</code> if it is a standalone.
	 * 
	 * @return The state of this object's membership in a network
	 */
	public default boolean isNetworked() {
		return this.getNetworkName() != null && !this.getNetworkName().isEmpty();
	}

}
