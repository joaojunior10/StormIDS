package monitor.connectors.kafka;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import monitor.connectors.ChannelSpecification;
import util.Response;

public class KafkaChannelSpecification implements ChannelSpecification {
	//Kafka API
    private Producer<String, Response> producer = null;
    
	public KafkaChannelSpecification(Producer<String, Response> producer){
		this.producer = producer;
		
	}
	public void send(Response objToSend) throws Exception {
		KeyedMessage<String, Response> data = new KeyedMessage<String, Response>("ResourceMonitorTopic", objToSend);
        producer.send( data);
	}

}
