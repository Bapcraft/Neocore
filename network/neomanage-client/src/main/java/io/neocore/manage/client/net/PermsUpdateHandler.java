package io.neocore.manage.client.net;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.host.permissions.PermissedPlayer;
import io.neocore.api.host.permissions.PermissionCollection;
import io.neocore.api.player.NeoPlayer;
import io.neocore.api.player.PlayerManager;
import io.neocore.api.player.permission.PermissionManager;
import io.neocore.common.permissions.PermissionManagerImpl;
import io.neocore.manage.proto.NeomanageProtocol.ClientMessage;
import io.neocore.manage.proto.NeomanageProtocol.UpdatePermissionsNotification;
import io.neocore.manage.proto.NeomanageProtocol.UpdatePermissionsNotification.ReloadExtent;

public class PermsUpdateHandler extends MessageHandler {

	@Override
	public void handle(NmServer sender, ClientMessage message) {
		
		UpdatePermissionsNotification upn = message.getPermsUpdateNotification();
		
		ReloadExtent extent = upn.getExtenrt();
		PermissionManager perms = NeocoreAPI.getAgent().getPermissionManager();
		
		if (extent == ReloadExtent.PLAYER_PERMS) {
			
			Consumer<NeoPlayer> reloadPermissionsAction = np -> {
				
				if (np.hasIdentity(PermissedPlayer.class)) {
					
					PermissedPlayer pp = np.getIdentity(PermissedPlayer.class);
					List<PermissionCollection> cols = new ArrayList<>(pp.getCollections());
					for (PermissionCollection col : cols) {
						
						// TODO Make this cleaner.
						if (col.hasTag(PermissionManagerImpl.COLLECTION_GROUP_CFG_TAG)) pp.removeCollection(col);
						
					}
					
					perms.assignPermissions(np);
					
				}
				
			};
			
			PlayerManager players = NeocoreAPI.getAgent().getPlayerManager();
			if (upn.hasSpecificUuid()) {
				
				UUID uuid = UUID.fromString(upn.getSpecificUuid());
				
				if (players.isPopulated(uuid)) {
					
					NeoPlayer np = players.getPlayer(uuid);
					reloadPermissionsAction.accept(np);
					
				}
				
			} else {
				players.getOnlinePlayers().forEach(reloadPermissionsAction);
			}
			
		} else if (extent == ReloadExtent.EVERYTHING) {
			perms.repopulatePermissions();
		} else {
			NeocoreAPI.getLogger().warning("Got a permission reload message from " + sender.getLabel() + ", but extent didn't make sense: " + extent);
		}
		
	}

}
