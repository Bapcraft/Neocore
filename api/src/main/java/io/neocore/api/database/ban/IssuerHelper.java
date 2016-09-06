package io.neocore.api.database.ban;

import java.util.UUID;

/**
 * Lazy serialization util for ban issuers.
 * 
 * @author treyzania
 */
public class IssuerHelper {
	
	public static String serialize(BanIssuer issuer) {
		
		Class<? extends BanIssuer> clazz = issuer.getClass();
		
		if (clazz.equals(NameLiteralIssuer.class)) {
			return "name:" + ((NameLiteralIssuer) issuer).name;
		} else if (clazz.equals(PlayerBanIssuer.class)) {
			return "uuid:" + ((PlayerBanIssuer) issuer).uuid;
		} else {
			return "name:undefined"; // Just say fuck it.
		}
		
	}
	
	public static BanIssuer deserialize(String str) {
		
		String[] parts = str.split(":", 2);
		String type = parts[0];
		
		if (type.equals("name")) {
			return new NameLiteralIssuer(parts[1]);
		} else if (type.equals("uuid")) {
			return new PlayerBanIssuer(UUID.fromString(parts[1]));
		} else {
			return new NameLiteralIssuer("undefined");
		}
		
	}
	
}
