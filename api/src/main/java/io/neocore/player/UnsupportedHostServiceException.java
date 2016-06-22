package io.neocore.player;

import io.neocore.host.HostService;

public class UnsupportedHostServiceException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 661677004692269937L;
	
	public UnsupportedHostServiceException(HostService service) {
		super("The Neocore host does not support the " + service.name() + " service.");
	}
	
}
