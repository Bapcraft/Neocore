package io.neocore.common.player;

import io.neocore.api.database.session.EndpointMove;
import io.neocore.api.database.session.ProxiedSession;
import io.neocore.api.database.session.Session;
import io.neocore.api.infrastructure.DownstreamTransferEvent;
import io.neocore.api.infrastructure.ProxyAcceptor;
import io.neocore.api.infrastructure.ProxyService;
import io.neocore.api.player.NeoPlayer;
import io.neocore.common.service.ServiceManagerImpl;

public class ProxyAcceptorImpl implements ProxyAcceptor {

	public ProxyAcceptorImpl(ServiceManagerImpl man) {
		man.registerRegistrationHandler(ProxyService.class,
				r -> ((ProxyService) r.getServiceProvider()).setAcceptor(this));
	}

	@Override
	public void onDownstreamTransfer(DownstreamTransferEvent event) {

		NeoPlayer player = event.getPlayer();

		if (player.hasIdentity(Session.class)) {

			Session sess = player.getSession();

			if (sess.isNetworked()) {

				ProxiedSession ps = sess.getAsProxiedSession();

				EndpointMove move = ps.createEndpointMove();
				move.setDestination(event.getDestination());

				move.dirty();
				ps.dirty();
				ps.flush();

			}

		}

	}

}
