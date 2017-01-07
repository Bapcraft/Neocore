package io.neocore.common.cmd;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import io.neocore.api.cmd.AbstractCommand;
import io.neocore.api.cmd.CmdSender;
import io.neocore.api.database.player.DatabasePlayer;
import io.neocore.api.host.login.ServerPlayer;
import io.neocore.api.host.permissions.PermissedPlayer;
import io.neocore.api.host.permissions.PermissionCollection;
import io.neocore.api.player.NeoPlayer;
import io.neocore.api.player.PlayerIdentity;
import io.neocore.api.player.group.Group;
import io.neocore.api.player.group.GroupMembership;
import io.neocore.common.player.CommonPlayerManager;
import io.neocore.common.player.PlayerLeaseImpl;
import io.neocore.common.player.PlayerManagerWrapperImpl;

public class CommandActiveUserManager extends AbstractCommand {
	
	private PlayerManagerWrapperImpl manager;
	private CommonPlayerManager assembler;
	
	public CommandActiveUserManager(PlayerManagerWrapperImpl man, CommonPlayerManager cpm) {
		
		super("activeusers");
		
		this.manager = man;
		this.assembler = cpm;
		
	}
	
	@Override
	public void onExecute(CmdSender sender, String[] args) {
		
		if (!sender.hasPermission("neocore.cmd.activeusers")) this.noPerms();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd@HH:mm:ss");
		
		Map<UUID, List<PlayerLeaseImpl>> leases = this.manager.getLeases();
		
		sender.sendMessage("Players Loaded (" + leases.size() + "):");
		for (Map.Entry<UUID, List<PlayerLeaseImpl>> entry : leases.entrySet()) {
			
			NeoPlayer np = this.assembler.findPlayer(entry.getKey());
			
			String username = "[unavailable]";
			if (np.hasIdentity(ServerPlayer.class)) {
				
				ServerPlayer sp = np.getIdentity(ServerPlayer.class);
				username = sp.isOnline() ? sp.getName() : "[error:unloading]";
				
			}
			
			sender.sendMessage(
				String.format(
					" - %s (%s) [%s] %s",
					username,
					np.getUniqueId().toString().substring(0, 8),
					np.isPopulated() ? "OK" : "LOADING",
					sdf.format(np.getConstructionTime())
				)
			);
			
			// Leases
			List<PlayerLeaseImpl> pls = entry.getValue();
			if (!pls.isEmpty()) {
				
				PlayerLeaseImpl pl0 = pls.get(0);
				StringBuilder sb = new StringBuilder("   - Leases (" + pls.size() + "): " + pl0.isValid() + "[" + sdf.format(pl0.getIssueTime()) + "]");
				for (int i = 1; i < pls.size(); i++) {
					
					PlayerLeaseImpl pli = pls.get(i);
					sb.append(", " + pli.isValid() + "[" + sdf.format(pli.getIssueTime()) + "]");
					
				}
				
				sender.sendMessage(sb.toString());
				
			}
			
			// Identities
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
			
			if (np.hasIdentity(DatabasePlayer.class)) {
				
				DatabasePlayer dbp = np.getIdentity(DatabasePlayer.class);
				List<GroupMembership> gms = dbp.getGroupMemberships();
				
				sender.sendMessage("   - Groups (" + gms.size() + "):");
				for (GroupMembership gm : gms) {
					
					Group g = gm.getGroup();
					sender.sendMessage("     - " + g.getDisplayName() + " &7" + g.getName());
					
				}
				
			}
			
			// Permissions
			if (np.hasIdentity(PermissedPlayer.class)) {
				
				PermissedPlayer pp = np.getIdentity(PermissedPlayer.class);
				List<PermissionCollection> cols = pp.getCollections();
				
				sender.sendMessage("   - Collections (" + cols.size() + "):");
				for (PermissionCollection col : cols) {
					
					sender.sendMessage("     (");
					
					Set<String> tags = col.getTags();
					StringBuilder tagSb = new StringBuilder("      - Tags:");
					for (String tag : tags) {
						tagSb.append(" " + tag);
					}
					
					sender.sendMessage(tagSb.toString());
					sender.sendMessage("      - Permissions: " + col.getPermissionsApplied().size());
					
					sender.sendMessage("     )");
					
				}
				
			}
			
		}
		
		this.success();
		
	}
	
}
