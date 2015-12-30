package monitor.connectors;

import util.Response;

public interface ChannelSpecification {
	 void send(Response objToSend) throws Exception;
}
