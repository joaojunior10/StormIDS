package monitor.connectors.kafka;

import java.net.InetAddress;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import monitor.connectors.ChannelSpecification;
import monitor.plugins.packetcapture.Response;

public class KafkaChannelSpecification implements ChannelSpecification {
	//Kafka API
    private Producer<String, Response> producer = null;
    
	public KafkaChannelSpecification(Producer<String, Response> producer){
		this.producer = producer;
		
	}
	public void send(Response objToSend) throws Exception {
		KeyedMessage<String, Response> data = new KeyedMessage<String, Response>("ResourceMonitorTopic",InetAddress.getLocalHost().getHostName(), objToSend);
        producer.send( data);
	}

}
