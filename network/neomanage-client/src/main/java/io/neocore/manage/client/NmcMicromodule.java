package io.neocore.manage.client;

import io.neocore.api.module.JavaMicromodule;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NmcMicromodule extends JavaMicromodule {
	
	private NioEventLoopGroup eventLoopGroup;
	private Bootstrap nettyBootstrap;
	
	@Override
	public void onEnable() {
		
		// Init
		this.eventLoopGroup = new NioEventLoopGroup();
		this.nettyBootstrap = new Bootstrap();
		
		// Setup configuration.
		this.nettyBootstrap.group(this.eventLoopGroup);
		this.nettyBootstrap.channel(NioSocketChannel.class);
		this.nettyBootstrap.handler(new ProtobufClientInitializer());
		
	}
	
	@Override
	public void onDisable() {
		
		// Be nice to the server.
		this.eventLoopGroup.shutdownGracefully();
		
	}
	
	public Bootstrap getBootstrap() {
		return this.nettyBootstrap;
	}
	
}
