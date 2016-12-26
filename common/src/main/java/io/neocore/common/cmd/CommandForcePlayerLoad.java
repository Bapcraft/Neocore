package io.neocore.common.cmd;

import java.util.UUID;

import io.neocore.api.cmd.AbstractCommand;
import io.neocore.api.cmd.CmdSender;
import io.neocore.api.host.Scheduler;
import io.neocore.api.player.PlayerLease;
import io.neocore.api.player.PlayerManager;

public class CommandForcePlayerLoad extends AbstractCommand {
	
	private PlayerManager manager;
	private Scheduler sched;
	
	public CommandForcePlayerLoad(PlayerManager pm, Scheduler s) {
		
		super("forceplayerload");
		
		this.manager = pm;
		this.sched = s;
		
	}
	
	@Override
	public void onExecute(CmdSender sender, String[] args) {
		
		if (!sender.hasPermission("neocore.cmd.forceplayerload")) this.noPerms();
		
		UUID uuid = UUID.fromString(args[0]);
		long duration = Long.parseLong(args[1]);
		
		PlayerLease lease = this.manager.requestLease(uuid);
		this.sched.invokeAsync(() -> {
			
			sender.sendMessage("ah balls");
			try {
				Thread.sleep(duration);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			sender.sendMessage("okay!");
			
			lease.release();
			
		});
		
		this.success();
		
	}

}
