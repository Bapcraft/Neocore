package io.neocore.api.host.proxy;

public interface ProxyAcceptor {
	
	public void onDownstreamTransfer(DownstreamTransferEvent event);
	
}
