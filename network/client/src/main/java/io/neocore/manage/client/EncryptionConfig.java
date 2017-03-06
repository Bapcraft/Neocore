package io.neocore.manage.client;

import java.security.PrivateKey;
import java.security.PublicKey;

public class EncryptionConfig {
	
	private PublicKey serverPublicKey;
	private PrivateKey localPrivateKey;
	
	public EncryptionConfig(PublicKey serverPub, PrivateKey localPriv) {
		
		this.serverPublicKey = serverPub;
		this.localPrivateKey = localPriv;
		
	}
	
	public PublicKey getRemotePublicKey() {
		return this.serverPublicKey;
	}
	
	public PrivateKey getLocalPrivateKey() {
		return this.localPrivateKey;
	}
	
}
