package monitor;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

import java.nio.channels.ClosedChannelException;

import javax.net.ssl.SSLException;

import monitor.connectors.SystemResourceMonitor;
import monitor.connectors.netty.NettyChannelSpecification;
import monitor.connectors.netty.NettyConnectionInitializer;
import monitor.plugins.*;
import monitor.plugins.prototype.SystemResourcePlugin;


public class NettySystemResourceMonitor extends SystemResourceMonitor{

	public NettySystemResourceMonitor(String host, int port) {
		super(host, port);
	}
	public static void main(String[] args) {
		//Creates the monitor
		NettySystemResourceMonitor monitor = new NettySystemResourceMonitor("127.0.0.1", 8992);
				
				//Add the plugins
				
				CpuUsagePlugin plugin1 = new CpuUsagePlugin(DEFAULT_SHORT_PROBE_TIME_INTERVAL);
				MemUsagePlugin plugin2 = new MemUsagePlugin(DEFAULT_SHORT_PROBE_TIME_INTERVAL);
				FileSystemUsagePlugin plugin3 = new FileSystemUsagePlugin(DEFAULT_SHORT_PROBE_TIME_INTERVAL);
				NetInfoPlugin plugin4 = new NetInfoPlugin(DEFAULT_LONG_PROBE_TIME_INTERVAL);
				CpuInfoPlugin plugin5 = new CpuInfoPlugin(DEFAULT_LONG_PROBE_TIME_INTERVAL);
				MemInfoPlugin plugin6 = new MemInfoPlugin(DEFAULT_LONG_PROBE_TIME_INTERVAL);
				NetRouteListPlugin plugin7 = new NetRouteListPlugin(DEFAULT_LONG_PROBE_TIME_INTERVAL);
				NetInterfaceStatsPlugin plugin8 = new NetInterfaceStatsPlugin(DEFAULT_LONG_PROBE_TIME_INTERVAL);
				ProcessListPlugin plugin9 = new ProcessListPlugin(DEFAULT_LONG_PROBE_TIME_INTERVAL);
				PacketDataPlugin plugin11 = new PacketDataPlugin(DEFAULT_SHORT_PROBE_TIME_INTERVAL);

//				monitor.addResourceMonitor(plugin1);
//				monitor.addResourceMonitor(plugin2);
//				monitor.addResourceMonitor(plugin3);
//				monitor.addResourceMonitor(plugin4);
//				monitor.addResourceMonitor(plugin5);
//				monitor.addResourceMonitor(plugin6);
//				monitor.addResourceMonitor(plugin7);
//				monitor.addResourceMonitor(plugin8);
				monitor.addResourceMonitor(plugin11);

				//Run the monitor;
				monitor.run();

	}
	
	public void run() {

		EventLoopGroup group = new NioEventLoopGroup();
		try {
			// Configure SSL.
			//TODO Change the insecure Trust Manager Factory for a chain of trusted certificates...
			final SslContext sslCtx = SslContext.newClientContext(InsecureTrustManagerFactory.INSTANCE);
			System.out.println("[Resource Monitor] Monitor Initialized");
			
			Bootstrap bootstrap = new Bootstrap()
			.group(group)
			.channel(NioSocketChannel.class)
			.handler(new NettyConnectionInitializer(sslCtx, host, port));

			Channel channel = bootstrap.connect(host, port).sync().channel();
			System.out.println("[Resource Monitor] Monitoring your resources...");
			//Each monitor plugin is a thread, they manage how often they will send the info to the server.
			for(SystemResourcePlugin plugin : listOfResourceMonitors) {
				plugin.setChannel(new NettyChannelSpecification( channel));
				new Thread(plugin).start();
				}
			while (true) {
				if(!channel.isActive()) {
	        		throw new ClosedChannelException();
	        		}
				
				Thread.sleep(DEFAULT_SHORT_PROBE_TIME_INTERVAL);
			}

		}
		catch (InterruptedException e) {
			e.printStackTrace();
			System.err.println("[Resource Monitor] Monitor Interrupted, shutting down the monitor.");
			restart(group);
		} catch (SSLException e) {
			e.printStackTrace();

			System.err.println("[Resource Monitor] Shutting down because it wasn't possible to establish a safe connection with the server :(");
			restart(group);
		} catch (ClosedChannelException e) {
			e.printStackTrace();

		 	System.err.println("[Resource Monitor] The plugins are telling me that the channel has dropped. I will try to fix that...");
		 	restart(group);
		} 
		catch ( Exception e) {
			e.printStackTrace();

			System.err.println(e.getLocalizedMessage());
			System.err.println("[Resource Monitor] Some problems occurred, shutting down the monitor.");
			restart(group);
		}
		
	}
	
	void restart(EventLoopGroup group){
		
		try {
			group.shutdownGracefully();
			System.err.println("[Resource Monitor] Retrying in " + DEFAULT_CHANNEL_CHECK_TIME_INTERVAL/1000 + " seconds...");
			Thread.sleep(DEFAULT_CHANNEL_CHECK_TIME_INTERVAL);
			run();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
}

