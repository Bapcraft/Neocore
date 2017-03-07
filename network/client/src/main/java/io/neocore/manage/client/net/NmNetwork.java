package io.neocore.manage.client.net;

import java.util.List;
import java.util.UUID;

public class NmNetwork {

	public List<NmServer> servers;

	private PingRunner pinger;
	private Thread pingerThread;

	public NmNetwork(UUID agentId, List<NmServer> servs, long pingPeriod) {

		this.servers = servs;

		this.pinger = new PingRunner(agentId, this, pingPeriod);
		this.pingerThread = new Thread(this.pinger, "PingQueueThread");
		this.pingerThread.start();

	}

	public NmServer getActiveServer() {

		for (NmServer serv : this.servers) {
			if (serv.isOnline())
				return serv;
		}

		throw new IllegalStateException("No Neomanage servers active!");

	}

	public void removeServer(NmServer serv) {
		this.servers.remove(serv);
	}

	public void shutdownConnections() {

		for (NmServer serv : this.servers) {
			serv.close();
		}

	}

}
