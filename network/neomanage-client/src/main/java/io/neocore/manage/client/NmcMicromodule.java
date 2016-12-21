package io.neocore.manage.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigValueType;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.infrastructure.InfrastructureService;
import io.neocore.api.module.JavaMicromodule;
import io.neocore.common.NeocoreImpl;
import io.neocore.manage.client.net.HandlerManager;
import io.neocore.manage.client.net.NmClient;
import io.neocore.manage.client.net.NmNetwork;
import io.neocore.manage.client.net.NmServer;
import io.neocore.manage.client.net.PingHandler;
import io.neocore.manage.client.net.PlayerUpdateHandler;
import io.neocore.manage.client.net.RemoteShutdownHandler;
import io.neocore.manage.client.network.DaemonNetworkMapService;
import io.neocore.manage.proto.NeomanageProtocol.ClientMessage.PayloadCase;
import io.neocore.manage.proto.NeomanageProtocol.ServerClient.ServerRole;
import io.neocore.manage.proto.NeomanageProtocol.ServerClient.ServerType;

public class NmcMicromodule extends JavaMicromodule {
	
	// Identity information.
	private EncryptionConfig cryptoConf;
	private List<InetSocketAddress> remotes = new ArrayList<>();
	private HandlerManager handlerManager;
	private NmClient client;
	private NmNetwork network;
	
	// Services.
	private DaemonNetworkMapService netMapService;
	
	// Sync utils.
	private NmdNetworkSync networkSync;
	
	@Override
	public void configure(Config config) {
		
		// Check for traffic encryption settings.
		if (config.getBoolean("use-crypto")) {
			
			String pub = config.getString("crypto.server-private-key");
			String priv = config.getString("crypto.local-public-key");
			
			try {
				
				KeyFactory fac = KeyFactory.getInstance("RSA");
				X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(pub.getBytes());
				PublicKey pubKey = fac.generatePublic(pubKeySpec);
				PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(priv.getBytes());
				PrivateKey privKey = fac.generatePrivate(privKeySpec);
				this.cryptoConf = new EncryptionConfig(pubKey, privKey);
				
			} catch (InvalidKeySpecException e) {
				NeocoreAPI.getLogger().log(Level.WARNING, "Bad key configuration!", e);
			} catch (NoSuchAlgorithmException e) {
				// uh, no.
			}
			
		}
		
		// Set up the list of daemons.
		config.getList("daemons").forEach(cv -> {
			
			if (cv.valueType() == ConfigValueType.STRING) {
				
				String[] parts = ((String) cv.unwrapped()).split(":", 2);
				InetSocketAddress addr = new InetSocketAddress(parts[0], Integer.parseInt(parts[1]));
				
				NeocoreAPI.getLogger().info("Using daemon at " + addr + "...");
				this.remotes.add(addr);
				
			}
			
		});
		
	}
	
	@Override
	public void onEnable() {
		
		// Setup.
		NeocoreImpl impl = (NeocoreImpl) this.getAgent();
		this.handlerManager = new HandlerManager();
		
		// Set up our view of the network.
		this.client = new NmClient(NeocoreAPI.getAgent(), this.handlerManager, ServerType.UNDEFINED, NeocoreAPI.isFrontend() ? ServerRole.FRONTEND : ServerRole.ENDPOINT);
		List<NmServer> servers = new ArrayList<>();
		this.network = new NmNetwork(impl.getAgentId(), servers, 15000L);
		
		// Set up connections.
		Logger log = NeocoreAPI.getLogger();
		for (InetSocketAddress addr : this.remotes) {
			
			log.info("Connecting to " + addr.toString() + " for Neomanage...");
			try {
				
				NmServer server = this.client.connect(addr, 10000, 120 * 1000L);
				this.network.servers.add(server);
				
			} catch (SocketTimeoutException e) {
				log.log(Level.SEVERE, "Could not connect to server!", e);
			} catch (IOException e) {
				log.log(Level.SEVERE, "Problem completing handshake!", e);
			}
			
		}
		
		// Set up services.
		this.netMapService = new DaemonNetworkMapService(NeocoreAPI.getNetworkName()); // FIXME Network name.
		
		// Register them.
		this.registerService(InfrastructureService.NETWORKMAP, this.netMapService);
		
		// Setup network sync.
		this.networkSync = new NmdNetworkSync(impl.getAgentId(), this.network);
		impl.getPlayerAssembler().overrideNetworkSync(this.networkSync);
		
		// Configure handler manager.
		this.handlerManager.setHandler(PayloadCase.DAEMONSHUTDOWN, new RemoteShutdownHandler(this.network));
		this.handlerManager.setHandler(PayloadCase.PING, new PingHandler());
		this.handlerManager.setHandler(PayloadCase.PLAYERUPDATE, new PlayerUpdateHandler(this.networkSync));
		
	}
	
	@Override
	public void onDisable() {
		
		// Security?  It doesn't hurt, at least.
		this.cryptoConf = null;
		
		this.network.shutdownConnections();
		
		System.gc();
		
	}
	
	public EncryptionConfig getCryptoConfig() {
		return this.cryptoConf;
	}
	
}
