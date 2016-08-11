package io.neocore.common.module.micro;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import io.neocore.api.module.JavaMicromodule;

public class MicromoduleClassLoader extends URLClassLoader {
	
	private final Class<? extends JavaMicromodule> mainClass;
	private final JavaMicromodule micromodule;
	
	public MicromoduleClassLoader(File micromodule, String mainClass, ClassLoader parent) throws MalformedURLException {
		
		super(new URL[] { micromodule.toURI().toURL() }, parent);
		
		try {
			
			Class<?> mainClassTest = Class.forName(mainClass, true, this);
			this.mainClass = mainClassTest.asSubclass(JavaMicromodule.class);
			
			// Needs a no-args constructor to work.
			this.micromodule = this.mainClass.newInstance();
			
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException("Could not instantiate classes properly!", e);
		} catch (InstantiationException e) {
			throw new NullPointerException("Main micromodule class for " + micromodule.getName() + " does not contain a no-args constructor!");
		} catch (IllegalAccessException e) {
			throw new NullPointerException("wtf?");
		}
		
	}
	
	public Class<? extends JavaMicromodule> getMainClass() {
		return this.mainClass;
	}
	
	public JavaMicromodule getMicromodule() {
		return this.micromodule;
	}
	
}
