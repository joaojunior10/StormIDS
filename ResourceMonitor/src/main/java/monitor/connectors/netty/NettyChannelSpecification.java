package monitor.connectors.netty;

import java.nio.channels.ClosedChannelException;

import monitor.connectors.ChannelSpecification;
import io.netty.channel.Channel;
import monitor.plugins.packetcapture.Response;

public class NettyChannelSpecification implements ChannelSpecification {
	Channel channel = null;
	public NettyChannelSpecification (Channel channel){
		this.channel = channel;
	}
	public void send(Response obj) throws ClosedChannelException {
		if(channel != null){
			if(!channel.isActive()) {
        		throw new ClosedChannelException();
        		}
		channel.writeAndFlush(obj.toString() + "\r\n");
		System.out.println("Sent to Network:"+ obj.toString() + "\r\n");
		}

	}

}
