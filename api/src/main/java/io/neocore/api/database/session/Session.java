package io.neocore.api.database.session;

import java.net.InetAddress;
import java.util.Date;

import io.neocore.api.database.PersistentPlayerIdentity;

public interface Session extends PersistentPlayerIdentity {

	public void setLoginUsername(String name);

	public String getLoginUsername();

	public void setAddress(InetAddress addr);

	public InetAddress getAddress();

	public void setHostString(String string);

	public String getHostString();

	public void setState(SessionState state);

	public SessionState getState();

	public void setFrontend(String frontend);

	public String getFrontend();

	public void setNetworked(boolean nw);

	public boolean isNetworked();

	public ProxiedSession getAsProxiedSession();

	public void setStartDate(Date start);

	public Date getStartDate();

	public void setEndDate(Date end);

	public Date getEndDate();

}
