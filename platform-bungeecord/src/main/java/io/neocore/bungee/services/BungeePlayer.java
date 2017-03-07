package io.neocore.bungee.services;

import java.util.Date;
import java.util.UUID;

import io.neocore.api.host.login.ServerPlayer;

public class BungeePlayer extends WrappedPlayer implements ServerPlayer {

	private Date contructionTime = new Date(); // Set to now on creation.

	public BungeePlayer(UUID uuid) {
		super(uuid);
	}

	@Override
	public String getName() {
		return this.getPlayerOrThrow().getName();
	}

	@Override
	public Date getLoginTime() {
		return this.contructionTime;
	}

}
