package io.neocore.api.event;

public @interface NeocoreEventHandler {

	int priority() default -1;
	
}
