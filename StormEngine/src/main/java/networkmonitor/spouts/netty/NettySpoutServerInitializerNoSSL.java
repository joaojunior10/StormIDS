package networkmonitor.spouts.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * Creates a newly configured {@link ChannelPipeline} for a new channel.
 */
public class NettySpoutServerInitializerNoSSL extends ChannelInitializer<SocketChannel> {

    NettySpoutNoSSL spout;

    public NettySpoutServerInitializerNoSSL(NettySpoutNoSSL spout2) {
        this.spout=spout2;
    }

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

    	pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
		pipeline.addLast("decoder", new StringDecoder());
		pipeline.addLast("encoder", new StringEncoder());

        // and then business logic.
		pipeline.addLast("handler",new NettySpoutServerHandlerNoSSL(spout));
    }
}
