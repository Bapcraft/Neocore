package io.neocore.common.cmd;

import java.util.Arrays;
import java.util.List;

import io.neocore.api.ServiceManager;
import io.neocore.api.cmd.AbstractCommand;
import io.neocore.api.cmd.CmdSender;
import io.neocore.api.host.broadcast.BroadcastService;

public class CommandBroadcast extends AbstractCommand {
	
	private ServiceManager services;
	
	public CommandBroadcast(ServiceManager serv) {
		
		super("broadcast");
		
		this.services = serv;
		
	}
	
	@Override
	public void onExecute(CmdSender sender, String[] args) {
		
		if (!sender.hasPermission("neocore.cmd.broadcast")) this.noPerms();
		if (args.length < 1) this.badUsage();
		
		StringBuilder sb = new StringBuilder(args[0]);
		for (int i = 1; i < args.length; i++) {
			sb.append(" " + args[i]);
		}
		
		BroadcastService serv = this.services.getService(BroadcastService.class);
		if (serv != null) {
			
			serv.broadcast(sb.toString());
			this.success();
			
		} else {
			
			sender.sendMessage("No broadcast service registered.");
			this.error();
			
		}
		
	}
	
	@Override
	public List<String> getAliases() {
		return Arrays.asList("bcast", "bc");
	}
	
}
