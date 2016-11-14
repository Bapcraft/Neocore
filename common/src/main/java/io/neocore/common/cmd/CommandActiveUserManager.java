package io.neocore.common.cmd;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;

import io.neocore.api.cmd.AbstractCommand;
import io.neocore.api.cmd.CmdSender;
import io.neocore.api.player.NeoPlayer;
import io.neocore.api.player.PlayerIdentity;
import io.neocore.api.player.PlayerManager;

public class CommandActiveUserManager extends AbstractCommand {
	
	private PlayerManager manager;
	
	public CommandActiveUserManager(PlayerManager man) {
		
		super("activeusers");
		
		this.manager = man;
		
	}
	
	@Override
	public void onExecute(CmdSender sender, String[] args) {
		
		if (!sender.hasPermission("neocore.cmd.activeusers")) this.noPerms();
		
		Set<NeoPlayer> players = this.manager.getOnlinePlayers();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd@HH:mm:ss");
		
		sender.sendMessage("Players Loaded (" + players.size() + "):");
		for (NeoPlayer np : players) {
			
			sender.sendMessage(
				String.format(
					" - %s (%s) [%s] %s",
					np.getUsername(),
					np.getUniqueId().toString().substring(0, 8),
					np.isPopulated() ? "OK" : "LOADING",
					sdf.format(np.getConstructionTime())
				)
			);
			
			List<PlayerIdentity> idents = np.getIdentities();
			if (!idents.isEmpty()) {
				
				PlayerIdentity id0 = idents.get(0);
				StringBuilder sb = new StringBuilder("   - " + (id0 != null ? id0.getClass().getSimpleName() : "null"));
				for (int i = 1; i < idents.size(); i++) {
					
					PlayerIdentity idi = idents.get(i);
					sb.append(", " + (idi != null ? idi.getClass().getSimpleName() : "null"));
					
				}
				
				sender.sendMessage(sb.toString());
				
			}
			
		}
		
		this.success();
		
	}
	
}
