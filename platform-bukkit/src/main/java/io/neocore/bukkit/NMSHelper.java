package io.neocore.bukkit;

import org.bukkit.Bukkit;

public class NMSHelper {
	
	// Help from: http://stackoverflow.com/questions/28703763/
	public static String getNmsPackageName() {
		
		String pkg = Bukkit.getServer().getClass().getPackage().getName();
		return pkg.substring(pkg.lastIndexOf('.') + 1);
		
	}
	
	public static Class<?> formatNmsClass(String sourcePackage, String simpleName) throws ClassNotFoundException {
		return Class.forName(String.format("%s.%s.%s", sourcePackage, getNmsPackageName(), simpleName));
	}
	
}
