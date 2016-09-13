package io.neocore.bukkit.cmd;

import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.cmd.AbstractCommand;
import io.neocore.bukkit.NMSHelper;

public class CommandInjector_19r2 extends CommandInjector {
	
	private CommandMap map;
	
	public CommandInjector_19r2() {
		
		try {
			
			Field f = NMSHelper.formatNmsClass("CraftServer").getDeclaredField("commandMap");
			boolean acc = f.isAccessible();
			f.setAccessible(true);
			this.map = (CommandMap) f.get(Bukkit.getServer());
			f.setAccessible(acc);
			
		} catch (ReflectiveOperationException e){
			NeocoreAPI.getLogger().severe("Error setting up command injector!  Neocore-rooted command probably won't work!");
		}
		
	}
	
	@Override
	public void inject(AbstractCommand cmd) {
		this.map.register(cmd.getName(), cmd.getPrefix(), new CommandWrapper(cmd));
	}
	
}
