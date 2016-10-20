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
import io.neocore.api.module.JavaMicromodule;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NmcMicromodule extends JavaMicromodule {
	
	private NioEventLoopGroup eventLoopGroup;
	private Bootstrap nettyBootstrap;
	
	private EncryptionConfig cryptoConf;
	private List<InetSocketAddress> remotes = new ArrayList<>();
	
	@Override
	public void configure(Config config) {
		
		// Check for traffic incryption settings.
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
		
		// Init
		this.eventLoopGroup = new NioEventLoopGroup();
		this.nettyBootstrap = new Bootstrap();
		
		// Setup configuration.
		this.nettyBootstrap.group(this.eventLoopGroup);
		this.nettyBootstrap.channel(NioSocketChannel.class);
		this.nettyBootstrap.handler(new ProtobufClientInitializer());
		
	}
	
	@Override
	public void onDisable() {
		
		// Be nice to the server.
		this.eventLoopGroup.shutdownGracefully();
		
		// Security?  It doesn't hurt (too much), at least.
		this.cryptoConf = null;
		System.gc();
		
	}
	
	public EncryptionConfig getCryptoConfig() {
		return this.cryptoConf;
	}
	
	public Bootstrap getBootstrap() {
		return this.nettyBootstrap;
	}
	
}
