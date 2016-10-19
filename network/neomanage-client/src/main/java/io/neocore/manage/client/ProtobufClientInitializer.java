package io.neocore.manage.client;

import io.neocore.manage.proto.NeomanageProtocol;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

public class ProtobufClientInitializer extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		
		/**
		 * Taken partly from:
		 * https://github.com/lohitvijayarenu/netty-protobuf/blob/master/src/main/java/org/demo/nettyprotobuf/DemoClientInitializer.java
		 */
		
		ChannelPipeline p = ch.pipeline();
		
		// Base settings.
		p.addLast(new ProtobufVarint32FrameDecoder());
		
		// Add protocol decoders.  Make it reflective?
		p.addLast(new ProtobufDecoder(NeomanageProtocol.RegisterClient.getDefaultInstance()));
		
		// Finalization?
		p.addLast(new ProtobufVarint32LengthFieldPrepender());
		p.addLast(new ProtobufEncoder());
		
		// TODO Add actual handlers.
		
	}

}
