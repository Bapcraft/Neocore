package io.neocore.module.micro;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import io.neocore.api.module.JavaMicromodule;
import io.neocore.api.module.Micromodule;
import io.neocore.api.module.Module;
import io.neocore.module.ModuleManagerImpl;

public class MicromoduleLoader {
	
	private final ModuleManagerImpl moduleManager;
	private final File micromoduleDirectory;
	
	private Map<File, MicromoduleClassLoader> loaders;
	private List<JavaMicromodule> modules;
	
	public MicromoduleLoader(ModuleManagerImpl man, File dir) {
		
		this.moduleManager = man;
		this.micromoduleDirectory = dir;
		
		this.loaders = new HashMap<>();
		
		if (!dir.isDirectory()) throw new IllegalArgumentException("Micromodule path is not a directory!");
		File[] micromodules = dir.listFiles();
		
		this.modules = new ArrayList<>();
		for (File f : micromodules) {
			
			String[] parts = f.getName().split("\\.");
			String ext = parts[parts.length - 1];
			
			if (!ext.equals("jar")) continue;
			this.modules.add(this.loadMicromodule(f));
			
		}
		
		// Quickly register them all at once.
		for (Module m : this.modules) {
			this.moduleManager.registerModule(m);
		}
		
	}
	
	private JavaMicromodule loadMicromodule(File jar) {
		
		if (!jar.exists()) throw new IllegalArgumentException("Tried to load a jar that doesn't exist.");
		
		// Get the config.
		Config conf = getConfigFromFile(jar);
		String mainClass = conf.getString("module.main");
		
		// Set up the class loader.
		MicromoduleClassLoader loader = null;
		try {
			
			loader = new MicromoduleClassLoader(jar, mainClass, this.getClass().getClassLoader());
			this.loaders.put(jar, loader);
			
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException("wat.", e);
		}
		
		// Pull out the general information
		String name = conf.getString("module.name");
		String version = conf.getString("module.version");
		
		// Apply the general information.
		JavaMicromodule jm = loader.getMicromodule();
		Class<? extends JavaMicromodule> modClass = jm.getClass();
		
		// Really roundabout way of defining these values.
		try {
			
			Field nameField = modClass.getField("name");
			Field verField = modClass.getField("version");
			
			boolean nameAcc = nameField.isAccessible();
			nameField.setAccessible(true);
			nameField.set(jm, name);
			nameField.setAccessible(nameAcc);
			
			boolean verAcc = verField.isAccessible();
			verField.setAccessible(true);
			verField.set(jm, version);
			verField.setAccessible(verAcc);
			
		} catch (Exception e) {
			throw new RuntimeException("Something went wrong when populating the micromodule classes.", e);
		}
		
		// Now that it's all loaded, we can invoke this.
		jm.onLoad();
		
		return jm;
		
	}
	
	private static Config getConfigFromFile(File file) {
		
		// Set up the zip file reader.
		ZipFile zf = null;
		try {
			zf = new ZipFile(file);
		} catch (IOException e) {
			throw new IllegalArgumentException("Could not open micromodule!", e);
		}
		
		// Find the entries in the zip file.
		Enumeration<? extends ZipEntry> entries = zf.entries();
		
		// Get the ZipEntry that we desire from the jar.
		ZipEntry confEntry = null;
		while (entries.hasMoreElements()) {
			
			confEntry = entries.nextElement();
			if (confEntry.getName().equals(Micromodule.MICROMODULE_CONFIGURATION_FILE)) break;
			
		}
		
		// If we haven't found the config at this point we just skip out.
		if (confEntry == null) {
			
			try {
				zf.close();
			} catch (IOException e) { /* Do nothing? */ }
			
			throw new NullPointerException("No configuration found for micromodule " + file.getName() + "!");
			
		}
		
		// Instantiate the config from the inputstream.
		try {
			
			InputStream confStream = zf.getInputStream(confEntry);
			Config conf = ConfigFactory.parseReader(new InputStreamReader(confStream));
			zf.close();
			
			return conf;
			
		} catch (Exception e) {
			throw new IllegalArgumentException("Could not load config from micromodule " + file.getName() + "!", e);
		}
		
	}
	
	public File getRootDirectory() {
		return this.micromoduleDirectory;
	}
	
}
