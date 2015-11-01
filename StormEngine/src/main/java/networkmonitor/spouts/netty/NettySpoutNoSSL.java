package networkmonitor.spouts.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.SelfSignedCertificate;

import java.util.Map;

import networkmonitor.spouts.NetworkMonitorSpout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.json.JSONObject;
import util.json.JSONTokener;
import backtype.storm.spout.ISpout;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IComponent;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;


//IRichSpout??
public class NettySpoutNoSSL extends NetworkMonitorSpout  {

	static private int PORT;
	public static final Logger LOG = LoggerFactory.getLogger(NettySpoutNoSSL.class);
	private static final long serialVersionUID = 1L;
	final NettySpoutNoSSL self = this;


	public NettySpoutNoSSL(int port) {
		this.PORT = port;
	}

	public void open(Map conf, TopologyContext context,
			SpoutOutputCollector collector) {
		
		setCollector(collector);
		//Thread to listen the Netty Clients.
		new Thread(new Runnable() {
			NettySpoutNoSSL spout = self;
			public void run() {

				while(true){
					EventLoopGroup bossGroup = new NioEventLoopGroup(1);
					EventLoopGroup workerGroup = new NioEventLoopGroup();
					try {
						
						ServerBootstrap b = new ServerBootstrap();
						b.group(bossGroup, workerGroup)
						.channel(NioServerSocketChannel.class)
						.childHandler(new NettySpoutServerInitializerNoSSL(spout));

						b.bind(PORT).sync().channel().closeFuture().sync();
					} catch (Exception e) {

						e.printStackTrace();
					}  finally {
						bossGroup.shutdownGracefully();
						workerGroup.shutdownGracefully();
					}
				}

			}
		}).start();
	}
	public void nextTuple() {

	}

	@Override
	public void ack(Object msgId) {
		send((Values)msgId);
	}

	@Override
	public void fail(Object msgId) {
		send((Values)msgId);
	}


	protected void messageReceived(ChannelHandlerContext ctx, String msg)
			throws Exception {
		Channel incoming = ctx.channel();
		
		//add to the JsonObject the source ip address.
//		String hostname = incoming.remoteAddress().toString();
//		jsonObj.put("hostname", hostname);
		
		send(new Values (msg));

		
	}



}