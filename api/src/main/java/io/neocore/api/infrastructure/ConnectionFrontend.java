package io.neocore.api.infrastructure;

/**
 * Represents something that players connect to that sends them gameplay
 * packets directly.  This is typically a BungeeCord server in networks, but
 * can be standalone servers.
 * 
 * @author treyzania
 */
public interface ConnectionFrontend extends NetworkMember {
	
}
