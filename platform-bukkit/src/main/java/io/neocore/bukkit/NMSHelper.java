package io.neocore.bukkit;

import org.bukkit.Bukkit;

public class NMSHelper {

	// Help from: http://stackoverflow.com/questions/28703763/
	public static String getNmsPackageName() {

		String pkg = Bukkit.getServer().getClass().getPackage().getName();
		return pkg.substring(pkg.lastIndexOf('.') + 1);

	}

	public static Class<?> formatNmsClass(String simpleName) throws ClassNotFoundException {

		String fullName = String.format("org.bukkit.craftbukkit.%s.%s", getNmsPackageName(), simpleName);

		try {
			return Class.forName(fullName);
		} catch (Exception e) {
			return Class.forName(fullName, true, Bukkit.class.getClassLoader());
		}

	}

}
