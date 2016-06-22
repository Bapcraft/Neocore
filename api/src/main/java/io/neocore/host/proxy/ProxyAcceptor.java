package io.neocore.host.proxy;

public interface ProxyAcceptor {
	
	public void onDownstreamTransfer(DownstreamTransferEvent event);
	
}
