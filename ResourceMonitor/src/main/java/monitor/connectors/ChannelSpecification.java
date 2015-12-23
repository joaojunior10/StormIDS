package monitor.connectors;

import monitor.plugins.packetcapture.Response;

public interface ChannelSpecification {
	 void send(Response objToSend) throws Exception;
}
