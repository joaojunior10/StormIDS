package monitor.connectors.kafka;

import java.net.InetAddress;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import monitor.connectors.ChannelSpecification;
import monitor.util.json.JSONObject;

public class KafkaChannelSpecification implements ChannelSpecification {
	//Kafka API
    private Producer<String, String> producer = null;
    
	public KafkaChannelSpecification(Producer<String, String> producer){
		this.producer = producer;
		
	}
	public void send(JSONObject objToSend) throws Exception {
		//KeyedMessage<String, String> data = new KeyedMessage<String, String>((String) objToSend.get("topic"),InetAddress.getLocalHost().getHostName(), objToSend.toString());
		KeyedMessage<String, String> data = new KeyedMessage<String, String>("ResourceMonitorTopic",InetAddress.getLocalHost().getHostName(), objToSend.toString());
        producer.send( data);
	}

}
