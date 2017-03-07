package io.neocore.bukkit.cmd;

import java.lang.reflect.Field;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;

import io.neocore.api.cmd.AbstractCommand;
import io.neocore.bukkit.NMSHelper;

public class CommandInjector_19r2 extends CommandInjector {

	private SimpleCommandMap map;
	private Map<String, Command> nativeCommandMap;

	@SuppressWarnings("unchecked")
	public CommandInjector_19r2() {

		try {

			Field f = NMSHelper.formatNmsClass("CraftServer").getDeclaredField("commandMap");
			f.setAccessible(true);

			Object o = f.get(Bukkit.getServer());
			if (o instanceof SimpleCommandMap) {

				this.map = (SimpleCommandMap) o;

				Field cmdField = this.map.getClass().getDeclaredField("knownCommands");
				cmdField.setAccessible(true);

				this.nativeCommandMap = (Map<String, Command>) cmdField.get(this.map);

			} else {
				Bukkit.getLogger().severe("Couldn't properly manipulate command map.");
			}

		} catch (ReflectiveOperationException e) {
			Bukkit.getLogger()
					.severe("Error setting up command injector!  Neocore-rooted commands probably won't work!");
		}

	}

	@Override
	public void inject(AbstractCommand cmd) {

		Command adding = new CommandWrapper(cmd);

		boolean res = this.map.register(cmd.getEndpointName(), cmd.getPrefix(), adding);
		if (cmd.isNativeOverride())
			this.nativeCommandMap.put(adding.getLabel(), adding);

		Bukkit.getLogger().info("Injected command " + cmd.getName() + " with result: " + res);

	}

}
