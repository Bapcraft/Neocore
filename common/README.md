# Neocore-Common

This project is the common library for the Neocore project.  It contains all of
the behind-the-scenes code necessary for Neocore host plugins to easily get the
Neocore API exposed and all of the subsystems running.

More documentation is in the works, but here's how you implement is as a Maven
dependency:

    <dependency>
    	<groupId>io.neocore</groupId>
    	<artifactId>neocore-common</artifactId>
    	<version>whatever</version>
    </dependency>

Check the `pom.xml` file to see what the version field should actually be.  But
in most cases you probably want to use the Neocore API as a dependency instead
of this project.  It has more stable interfaces and doesn't change as often. 
