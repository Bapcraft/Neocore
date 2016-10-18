package io.neocore.api.database;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * To be placed on a database IdentityProvider, specifying that it should be
 * loaded from the database asynchronously, but does not enforce this. 
 * 
 * @author treyzania
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface LoadAsync {

}
