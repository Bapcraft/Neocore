package io.neocore.api.infrastructure;

public interface ProxyService extends InfraServiceProvider {

	public void setAcceptor(ProxyAcceptor acc);

	public ProxyAcceptor getAcceptor();

}
