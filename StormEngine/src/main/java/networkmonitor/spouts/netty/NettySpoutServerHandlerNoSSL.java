package networkmonitor.spouts.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * Handles a server-side channel.
 */
public class NettySpoutServerHandlerNoSSL extends SimpleChannelInboundHandler<String> {

    static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    NettySpoutNoSSL spout;
    
    public NettySpoutServerHandlerNoSSL(NettySpoutNoSSL spout2) {
    	super();
		this.spout = spout2;
	}
   	
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception{
		Channel incoming = ctx.channel();
		
		for(Channel channel : channels){
			channel.write("[SERVER] - " + incoming.remoteAddress() + "has joined\n");
		}
		
		channels.add(ctx.channel());
		
	}
	
	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		Channel leaving = ctx.channel();
		
		for(Channel channel : channels){
			channel.write("[SERVER] - " + leaving.remoteAddress() + "has left\n");
		}
		
		channels.remove(ctx.channel());
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
