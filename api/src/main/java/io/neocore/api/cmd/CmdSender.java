package io.neocore.api.cmd;

import io.neocore.api.player.PlayerIdentity;

public interface CmdSender extends PlayerIdentity {

	public boolean isPlayer();

	public String getUsername();

	public boolean isOp();

	public boolean hasPermission(String perm);

	public void sendMessage(String str);

}
