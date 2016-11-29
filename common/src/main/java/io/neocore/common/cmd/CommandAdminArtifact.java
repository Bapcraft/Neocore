package io.neocore.common.cmd;

import java.util.UUID;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.ServiceManager;
import io.neocore.api.cmd.AbstractServiceCommand;
import io.neocore.api.cmd.CmdSender;
import io.neocore.api.database.artifact.Artifact;
import io.neocore.api.database.artifact.ArtifactService;
import io.neocore.api.player.NeoPlayer;

public class CommandAdminArtifact extends AbstractServiceCommand {
	
	private String artifactType;
	private String permission;
	
	public CommandAdminArtifact(ServiceManager man, String name, String type, String perm) {
		
		super(name, man);
		
		this.artifactType = type;
		this.permission = perm;
		
	}

	@Override
	public void onExecute(CmdSender sender, String[] args) {
		
		if (!sender.hasPermission(this.permission)) this.noPerms();
		if (args.length < 2) this.badUsage();
		
		String ident = args[0];
		StringBuilder reason = new StringBuilder(args[1]);
		for (int i = 2; i < args.length; i++) {
			reason.append(args[i]);
		}
		
		NeoPlayer np = null;
		try {
			np = NeocoreAPI.getPlayer(UUID.fromString(ident));
		} catch (IllegalArgumentException e) {
			np = NeocoreAPI.getPlayer(ident);
		}
		
		if (np == null) {
			
			sender.sendMessage("That player doesn't seem to be online.");
			this.error();
			
		}
		
		ArtifactService man = this.getService(ArtifactService.class);
		Artifact a = man.createArtifact(np.getUniqueId(), this.artifactType, reason.toString());
		sender.sendMessage("Artifact record created: " + a.getUniqueId());
		this.success();
		
	}

}
