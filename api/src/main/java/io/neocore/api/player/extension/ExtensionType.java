package io.neocore.api.player.extension;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Must be placed on all Extension classes to specify the name.  Should not be
 * modified after the fact or could lead to some unpredictable/uncomfortable
 * behavior by no fault of the database.
 * 
 * @author treyzania
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface ExtensionType {

	String name();
	Class<? extends ExtensionBuilder> builder();

}
