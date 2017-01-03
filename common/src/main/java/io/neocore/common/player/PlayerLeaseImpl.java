package io.neocore.common.player;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;
import java.util.logging.Logger;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.player.NeoPlayer;
import io.neocore.api.player.PlayerLease;

public class PlayerLeaseImpl implements PlayerLease {
	
	private final UUID uuid;
	private final Date issueTime = new Date();
	
	private PlayerManagerWrapperImpl manager;
	private CommonPlayerManager assembler;
	
	private boolean released = false;
	private CountDownLatch waitForCompleted = new CountDownLatch(1);
	
	private List<Consumer<NeoPlayer>> callbacks = new ArrayList<>();
	
	public PlayerLeaseImpl(UUID uuid, PlayerManagerWrapperImpl wrapper, CommonPlayerManager assembler) {
		
		this.uuid = uuid;
		this.manager = wrapper;
		this.assembler = assembler;
		
	}
	
	@Override
	public UUID getUniqueId() {
		return this.uuid;
	}
	
	@Override
	public NeoPlayer getPlayer() {
		return this.manager.getPlayer(this.uuid);
	}
	
	@Override
	public NeoPlayer getPlayerEventually() throws InterruptedException {
		
		this.assembler.awaitPlayerPopulation(this.uuid);
		return this.getPlayer();
		
	}
	
	@Override
	public void addCallback(Consumer<NeoPlayer> callback) {
		
		synchronized (this) {
			
			if (this.callbacks != null) {
				this.callbacks.add(callback);
			} else {
				callback.accept(this.getPlayer());
			}
			
		}
		
	}
	
	@Override
	public Date getIssueTime() {
		return this.issueTime;
	}
	
	@Override
	public void release() {
		
		synchronized (this) {
			
			Logger log = NeocoreAPI.getLogger();
			log.finest("Lease release requested! (" + this.uuid + ")");
			
			if (!this.released) {
				
				this.manager.releaseLease(this);
				this.released = true;
				
			} else {
				
				log.warning("Requested release of lease that was alredy released for " + this.uuid + "!");
				throw new IllegalStateException("Already-released lease was attempted to be released again.");
				
			}
			
		}
		
	}
	
	@Override
	public boolean isValid() {
		return !this.released;
	}
	
	protected void flagCompleted() {
		
		synchronized (this) {
			
			this.waitForCompleted.countDown();
			
			try {
				
				NeoPlayer player = this.getPlayerEventually();
				
				for (Consumer<NeoPlayer> cb : this.callbacks) {
					cb.accept(player);
				}
				
			} catch (InterruptedException e) {
				NeocoreAPI.getLogger().warning("Interrupted during wait for completion for " + this.uuid + " in lease!");
			}
			
			this.callbacks = null;
			
		}
		
	}
	
}
