package io.neocore.common.cmd;

import java.util.List;

import io.neocore.api.ServiceManager;
import io.neocore.api.cmd.AbstractServiceCommand;
import io.neocore.api.cmd.CmdSender;
import io.neocore.api.database.artifact.Artifact;
import io.neocore.api.database.artifact.ArtifactService;

public class CommandArtifactManager extends AbstractServiceCommand {

	public CommandArtifactManager(ServiceManager serv) {
		super("artifacts", serv);
	}

	@Override
	public void onExecute(CmdSender sender, String[] args) {

		if (!sender.hasPermission("neocore.cmd.artifacts"))
			this.noPerms();
		if (args.length < 1)
			this.badUsage();

		String subcommand = args[0];

		// Encapsulation.
		ArtifactService arts = this.getService(ArtifactService.class);

		if (subcommand.equals("create")) {

			if (args.length != 3)
				this.badUsage();

			String type = args[1];
			String data = args[2];

			Artifact art = arts.createArtifact(type, data);
			if (art != null) {

				sender.sendMessage("Created artifact: " + art.getUniqueId());
				this.success();

			} else {

				sender.sendMessage("Problem creating artifact!");
				this.error();

			}

		} else if (subcommand.equals("findtype")) {

			if (args.length != 2)
				this.badUsage();

			String type = args[1];

			List<Artifact> artList = arts.getArtifacts(type);
			if (artList != null) {

				sender.sendMessage("Artifacts (" + artList.size() + "):");
				for (Artifact art : artList) {
					sender.sendMessage(" - " + art.getUniqueId().toString().substring(0, 8) + "... - " + art.getData());
				}

				this.success();

			} else {

				sender.sendMessage("Problem querying artifacts!");
				this.error();

			}

		} else {
			this.badUsage();
		}

	}

}
