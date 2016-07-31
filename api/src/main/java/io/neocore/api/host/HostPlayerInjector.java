package io.neocore.api.host;

import java.util.function.Supplier;

import io.neocore.api.player.NeoPlayer;
import io.neocore.api.player.permission.DynamicPermissionCollection;

// This feels like it could get really messy really quick.
public interface HostPlayerInjector {
	
	public Supplier<DynamicPermissionCollection> injectPermissions(NeoPlayer player);
	
}
