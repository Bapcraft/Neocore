package io.neocore.jdbc;

import com.j256.ormlite.support.ConnectionSource;

public class AbstractJdbcService {
	
	private ConnectionSource source;
	
	public AbstractJdbcService(ConnectionSource src) {
		this.source = src;
	}
	
	public ConnectionSource getSource() {
		return this.source;
	}
	
}
