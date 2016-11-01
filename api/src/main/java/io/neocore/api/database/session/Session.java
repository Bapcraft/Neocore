package io.neocore.api.database.session;

import java.net.InetAddress;
import java.util.Date;

import io.neocore.api.player.PlayerIdentity;

public interface Session extends PlayerIdentity {
	
	public String getLoginUsername();
	
	public InetAddress getAddress();
	public String getHostString();
	
	public void setState(SessionState state);
	public SessionState getState();
	
	public void setFrontend(String frontend);
	public String getFrontend();
	
	public boolean isNetworked();
	public ProxiedSession getAsProxiedSession();
	
	public void setStartDate(Date start);
	public Date getStartDate();
	
	public void setEndDate(Date end);
	public Date getEndDate();
	
}
