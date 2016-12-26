package io.neocore.common.player;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

import com.treyzania.jzania.ExoContainer;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.player.NeoPlayer;
import io.neocore.api.player.PlayerLease;

public class PlayerLeaseImpl implements PlayerLease {
	
	private final ExoContainer exo = new ExoContainer(NeocoreAPI.getLogger());
	
	private final UUID uuid;
	private final Date issueTime = new Date();
	
	private PlayerManagerWrapperImpl manager;
	
	private boolean released = false;
	private CountDownLatch waitForCompleted = new CountDownLatch(1);
	
	private List<Consumer<NeoPlayer>> callbacks = new ArrayList<>();
	
	public PlayerLeaseImpl(UUID uuid, PlayerManagerWrapperImpl wrapper) {
		
		this.uuid = uuid;
		this.manager = wrapper;
		
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
		
		this.waitForCompleted.await();
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
			
			NeocoreAPI.getLogger().finest("Lease release requested! (" + this.uuid + ")");
			if (!this.released) {
				
				this.manager.releaseLease(this);
				this.released = true;
				
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
			
			NeoPlayer player = this.getPlayer();
			for (Consumer<NeoPlayer> cb : this.callbacks) {
				this.exo.invoke("LeaseCallback(" + this.uuid + ")", () -> cb.accept(player));
			}
			
			this.callbacks = null;
			
		}
		
	}
	
}
