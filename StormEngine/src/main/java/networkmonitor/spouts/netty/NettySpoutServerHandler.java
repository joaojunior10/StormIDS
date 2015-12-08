package networkmonitor.spouts.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.ssl.SslHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.net.InetAddress;

/**
 * Handles a server-side channel.
 */
public class NettySpoutServerHandler extends SimpleChannelInboundHandler<String> {

    static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    NettySpout spout;
    public NettySpoutServerHandler(NettySpout spout) {
		this.spout = spout;
	}
    @Override
    public void channelActive(final ChannelHandlerContext ctx) {
        // Once session is secured, send a greeting and register the channel to the global channel
        // list so the channel received the messages from others.
        ctx.pipeline().get(SslHandler.class).handshakeFuture().addListener(
                new GenericFutureListener<Future<Channel>>() {
                    public void operationComplete(Future<Channel> future) throws Exception {
                        ctx.writeAndFlush(
                                "Welcome to " + InetAddress.getLocalHost().getHostName() + " secure remote monitoring service!\n");
                        ctx.writeAndFlush(
                                "Your session is protected by " +
                                        ctx.pipeline().get(SslHandler.class).engine().getSession().getCipherSuite() +
                                        " cipher suite.\n");

                        channels.add(ctx.channel());
                    }
        });
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

	@Override
	// Message Receiver
	protected void channelRead0(ChannelHandlerContext ctx, String msg)
			throws Exception {
		spout.messageReceived(ctx, msg);
//		Channel incoming = ctx.channel();
//		//System.out.println("[" + incoming.remoteAddress() + "] " + msg + '\n');
//		String host = incoming.remoteAddress().toString();
//		Values message = new Values(host, msg);
//		spout.feedQueue.add(message);
//				
//		// Close the connection if the client has sent 'bye'.
//        if ("bye".equals(msg.toLowerCase())) {
//            ctx.close();
//        }
	}
}
