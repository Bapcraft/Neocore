package io.neocore.common.cmd;

import java.util.Arrays;
import java.util.List;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.ServiceManager;
import io.neocore.api.cmd.AbstractServiceCommand;
import io.neocore.api.cmd.CmdSender;
import io.neocore.api.database.artifact.Artifact;
import io.neocore.api.database.artifact.ArtifactService;
import io.neocore.api.database.artifact.ArtifactTypes;
import io.neocore.api.host.broadcast.BroadcastService;

public class CommandBroadcast extends AbstractServiceCommand {
	
	public CommandBroadcast(ServiceManager serv) {
		super("broadcast", serv);
	}
	
	@Override
	public void onExecute(CmdSender sender, String[] args) {
		
		if (!sender.hasPermission("neocore.cmd.broadcast")) this.noPerms();
		if (args.length < 1) this.badUsage();
		
		StringBuilder sb = new StringBuilder(args[0]);
		for (int i = 1; i < args.length; i++) {
			sb.append(" " + args[i]);
		}
		
		String prefix = "";
		try {
			
			ArtifactService am = this.getService(ArtifactService.class);
			Artifact prefixArt = am.getSingletonArtifact(ArtifactTypes.BCAST_DEFAULT_PREFIX);
			
			if (prefixArt != null) prefix = prefixArt.getData();
			
		} catch (UnsupportedOperationException e) {
			NeocoreAPI.getLogger().warning("You should set a default broadcast prefix.");
		}
		
		BroadcastService serv = this.getService(BroadcastService.class);
		if (serv != null) {
			
			serv.broadcast(prefix + sb.toString());
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
