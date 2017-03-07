package io.neocore.manage.client.net;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.ServiceManager;
import io.neocore.api.database.ban.BanService;
import io.neocore.manage.proto.NeomanageProtocol.ClientMessage;

public class BanUpdateHandler extends MessageHandler {

	@Override
	public void handle(NmServer sender, ClientMessage message) {

		ServiceManager sm = NeocoreAPI.getAgent().getServiceManager();
		BanService bs = sm.getService(BanService.class);
		if (bs != null)
			bs.reloadBans();

	}

}
