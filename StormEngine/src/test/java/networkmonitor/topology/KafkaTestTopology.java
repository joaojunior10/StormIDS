package networkmonitor.topology;

import backtype.storm.Config;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.topology.TopologyBuilder;
import networkmonitor.bolts.TesterBolt;
import networkmonitor.spouts.kafka.KafkaSpout;
import storm.kafka.BrokerHosts;
import storm.kafka.SpoutConfig;
import storm.kafka.StringScheme;
import storm.kafka.ZkHosts;
import util.storm.StormRunner;
public class KafkaTestTopology {

	public static void main(String[] args) {

		TopologyBuilder builder = createTopology();

		Config config = new Config();
		config.setDebug(true);
		//config.setMaxSpoutPending(1);
		try {
			StormRunner.runTopologyLocally(builder.createTopology(),
					"NetworkMonitor - Kafka Topology", config, 0);
		} catch (InterruptedException e) {
			System.out.println("\n\n Execution interrupted. \n\n");
		}


	}

	static private TopologyBuilder createTopology() {
		BrokerHosts brokerHosts = new ZkHosts("localhost:2181");
		String topicName = "ResourceMonitorTopic";
		SpoutConfig kafkaConfig = new SpoutConfig(brokerHosts, topicName, "/" + topicName, "GuiwyStormEngine");

		kafkaConfig.scheme = new SchemeAsMultiScheme(new StringScheme());


		TopologyBuilder topology = new TopologyBuilder();
		topology.setSpout("KafkaSpout", new KafkaSpout(kafkaConfig), 1);


		topology.setBolt("TesterBolt", new TesterBolt(), 1).shuffleGrouping("KafkaSpout");
			
		return topology;

	}

}
