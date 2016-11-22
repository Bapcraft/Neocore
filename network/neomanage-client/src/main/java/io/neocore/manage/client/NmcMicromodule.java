package io.neocore.manage.client;

import java.net.InetSocketAddress;
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

import com.typesafe.config.Config;
import com.typesafe.config.ConfigValueType;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.infrastructure.InfrastructureService;
import io.neocore.api.module.JavaMicromodule;
import io.neocore.common.NeocoreImpl;
import io.neocore.common.player.NetworkSync;
import io.neocore.manage.client.network.DaemonNetworkMapService;

public class NmcMicromodule extends JavaMicromodule {
	
	// Identity information.
	private EncryptionConfig cryptoConf;
	private List<InetSocketAddress> remotes = new ArrayList<>();
	private NmNetwork network;
	
	// Services.
	private DaemonNetworkMapService netMapService;
	
	// Sync utils.
	private NetworkSync networkSync;
	
	@Override
	public void configure(Config config) {
		
		// Check for traffic encryption settings.
		if (config.getBoolean("useCrypto")) {
			
			String pub = config.getString("serverPrivateKey");
			String priv = config.getString("localPublicKey");
			
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
		
		// Set up our view of the network.
		List<NmServer> servers = new ArrayList<>();
		this.remotes.forEach(a -> servers.add(new NmServer(a)));
		this.network = new NmNetwork(servers);
		
		// Set up connections.
		// TODO Connection.
		
		// Set up services.
		this.netMapService = new DaemonNetworkMapService(NeocoreAPI.getServerName());
		
		// Register them.
		this.registerService(InfrastructureService.NETWORKMAP, this.netMapService);
		
		// Set up sync.
		this.networkSync = new NmdNetworkSync(this.network);
		NeocoreImpl impl = (NeocoreImpl) NeocoreAPI.getAgent();
		impl.getPlayerAssembler().overrideNetworkSync(this.networkSync);
		
	}
	
	@Override
	public void onDisable() {
		
		// Security?  It doesn't hurt (too much), at least.
		this.cryptoConf = null;
		System.gc();
		
	}
	
	public EncryptionConfig getCryptoConfig() {
		return this.cryptoConf;
	}
	
}
