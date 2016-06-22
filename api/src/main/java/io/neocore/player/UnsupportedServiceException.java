package io.neocore.player;

import io.neocore.ServiceType;

public class UnsupportedServiceException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 661677004692269937L;
	
	public UnsupportedServiceException(ServiceType service) {
		super("The Neocore instance is not configured to support the " + service.getName() + " service.");
	}
	
}
