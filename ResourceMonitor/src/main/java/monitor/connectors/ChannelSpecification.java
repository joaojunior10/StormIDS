package monitor.connectors;


import com.google.gson.JsonObject;

public interface ChannelSpecification {
	 void send(String objToSend) throws Exception;
}
