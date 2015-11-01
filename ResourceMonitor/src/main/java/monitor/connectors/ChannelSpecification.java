package monitor.connectors;

import monitor.util.json.JSONObject;

public interface ChannelSpecification {
	 void send(JSONObject objToSend) throws Exception;
}
