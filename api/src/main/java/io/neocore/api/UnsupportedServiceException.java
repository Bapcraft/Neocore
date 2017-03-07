package io.neocore.api;

/**
 * Thrown when a service is not supported by Neocore, but is attempted to be
 * used.
 * 
 * @author treyzania
 */
public class UnsupportedServiceException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 661677004692269937L;

	public UnsupportedServiceException(ServiceType service) {
		super("The Neocore instance is not configured to support the " + service.getName() + " service.");
	}

}
