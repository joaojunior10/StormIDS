package monitor.connectors.kafka;

import java.net.InetAddress;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import monitor.connectors.ChannelSpecification;

public class KafkaChannelSpecification implements ChannelSpecification {
	//Kafka API
    private Producer<String, String> producer = null;
    
	public KafkaChannelSpecification(Producer<String, String> producer){
		this.producer = producer;
		
	}
	public void send(String objToSend) throws Exception {
		KeyedMessage<String, String> data = new KeyedMessage<String, String>("ResourceMonitorTopic",InetAddress.getLocalHost().getHostName(), objToSend);
        producer.send( data);
	}

}
