package io.neocore.common.cmd;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.UUID;

import com.google.common.collect.Iterators;

import io.neocore.api.NeocoreInstaller;
import io.neocore.api.ServiceManager;
import io.neocore.api.cmd.AbstractServiceCommand;
import io.neocore.api.cmd.CmdSender;
import io.neocore.api.database.ban.BanEntry;
import io.neocore.api.database.ban.BanService;
import io.neocore.api.database.player.PlayerService;
import io.neocore.api.host.Context;
import io.neocore.api.host.login.LoginService;
import io.neocore.api.host.login.ServerPlayer;

public class CommandBan extends AbstractServiceCommand {
	
	private static final String INFINITE_WORD = "infinite";
	
	public CommandBan(ServiceManager sm) {
		super("ban", sm);
	}

	@Override
	public void onExecute(CmdSender sender, String[] args) {
		
		if (!sender.hasPermission("neocore.cmd.ban")) this.noPerms();
		
		Iterator<String> iter = Iterators.forArray(args);
		
		BanService bans = this.getService(BanService.class);
		if (bans == null) {
			
			sender.sendMessage("There is no ban service available to register the ban.");
			this.error();
			
		}
		
		if (!iter.hasNext()) this.badUsage();
		String bannedQuery = iter.next();
		UUID uuid = null;
		
		try {
			
			uuid = UUID.fromString(bannedQuery);
			
		} catch (IllegalArgumentException e) {
			
			LoginService login = this.getService(LoginService.class);
			ServerPlayer sp = login.findPlayerByName(bannedQuery);
			
			if (sp != null) {
				uuid = sp.getUniqueId();
			} else {
				
				PlayerService players = this.getService(PlayerService.class);
				UUID dbid = new UUID(0, 0);
				
				try {
					dbid = players.resolveUUIDs(bannedQuery).get(0);
				} catch (IOException ioe) {
					
					// TODO
					ioe.printStackTrace();
					
				}
				
				if (dbid != null) {
					uuid = dbid;
				} else {
					sender.sendMessage("Can't find player: " + bannedQuery);
				}
				
			}
			
		}
		
		if (uuid == null) this.error();
		
		String durationStr;
		if (iter.hasNext()) {
			durationStr = iter.next();
		} else {
			durationStr = INFINITE_WORD;
		}
		
		Context ctx = Context.GLOBAL;
		String reason = null;
		
		if (iter.hasNext()) {
			
			StringBuilder sb = new StringBuilder();
			String here = iter.next();
			if (here.startsWith("@")) {
				ctx = Context.create(here.substring(1));
			} else {
				sb.append(here);
			}
			
			while (iter.hasNext()) {
				sb.append(" " + iter.next());
			}
			
			reason = sb.toString().trim();
			reason = !reason.isEmpty() && !reason.equalsIgnoreCase("null") ? reason : "&ono reason";
			
		}
		
		BanEntry be = bans.createNewBan(uuid);
		be.setContext(ctx);
		be.setDateIssued(new Date());
		be.setStartDate(new Date());
		be.setReason(reason);
		
		if (!durationStr.equalsIgnoreCase(INFINITE_WORD)) {
			
			char last = durationStr.charAt(durationStr.length() - 1);
			String number = durationStr.substring(0, durationStr.length() - 1);
			
			long multiplier = 1000;
			
			switch (last) {
			
			case 's':
				multiplier = 1000;
				break;
			
			case 'm':
				multiplier = 1000 * 60;
				break;
			
			case 'h':
				multiplier = 1000 * 60 * 60;
				break;
				
			case 'd':
				multiplier = 1000 * 60 * 60 * 24;
				break;
			
			case 'w':
				multiplier = 1000 * 60 * 60 * 24 * 7;
				break;
			
			case 'M':
				multiplier = 1000 * 60 * 60 * 24 * 30;
				break;
			
			case 'y':
				multiplier = 1000 * 60 * 60 * 24 * 265;
				break;
			
			default:
				sender.sendMessage("Bad time specifier: " + last);
				this.badUsage();
				break;
			
			}
			
			long qty = -1;
			
			try {
				
				qty = Long.parseLong(number);
				
			} catch (NumberFormatException e) {
				
				sender.sendMessage("Not a number: " + number);
				this.badUsage();
				
			}
			
			if (qty <= 0) {
				
				sender.sendMessage("Not a valid duration number: " + qty);
				this.badUsage();
				
			}
			
			be.setExpirationDate(new Date(be.getStartDate().getTime() + qty * multiplier));
			
		} else {
			be.setExpirationDate(null); // Infinite.
		}
		
		bans.addBan(be);
		bans.flushBans();
		
		// FIXME Eww strong coupling!
		NeocoreInstaller.installed.getNetworkManager().getNetworkSync().announceBansReload();
		
	}

	@Override
	public boolean isNativeOverride() {
		return true;
	}
	
	@Override
	public String getEndpointName() {
		return "neoban"; // Bukkit makes me sad.
	}

	@Override
	public String getProxyName() {
		return "ban";
	}
	
}
