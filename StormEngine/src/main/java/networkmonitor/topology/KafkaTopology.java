package networkmonitor.topology;

import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import networkmonitor.bolts.MultiplexerBolt;
import networkmonitor.bolts.PrettyPrinterBolt;
import networkmonitor.bolts.networkdata.NetworkDataBolt;
import networkmonitor.bolts.storage.LogMatchesBolt;
import networkmonitor.spouts.kafka.KafkaSpout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import storm.kafka.BrokerHosts;
import storm.kafka.SpoutConfig;
import storm.kafka.StringScheme;
import storm.kafka.ZkHosts;
import util.storm.StormRunner;

public class KafkaTopology{
    private static final Logger LOG = LoggerFactory.getLogger("reportsLogger");

	public static void main(String[] args) {

        LOG.trace("Application initiated");
		TopologyBuilder builder = createTopology();

		Config config = new Config();
		config.setDebug(true);
		config.put("cassandra.keyspace","stormids");
		config.setNumWorkers(2);

		//config.setMaxSpoutPending(1);
		try {
			StormSubmitter.submitTopology("NetworkMonitor - Kafka Topology", config, builder.createTopology());
		} catch (AlreadyAliveException e) {
			e.printStackTrace();
		} catch (InvalidTopologyException e) {
			e.printStackTrace();
		}

//		try {
//			StormRunner.runTopologyLocally(builder.createTopology(),
//					"NetworkMonitor - Kafka Topology", config, 0);
//		} catch (InterruptedException e) {
//			LOG.error("\n\n Execution interrupted. \n\n");
//		}
	}

	static private TopologyBuilder createTopology() {
		BrokerHosts brokerHosts = new ZkHosts("localhost:2181");
		String topicName = "ResourceMonitorTopic";
		SpoutConfig kafkaConfig = new SpoutConfig(brokerHosts, topicName, "/" + topicName, "GuiwyStormEngine");

		kafkaConfig.scheme = new SchemeAsMultiScheme(new StringScheme());


		TopologyBuilder topology = new TopologyBuilder();
		topology.setSpout("KafkaSpout", new KafkaSpout(kafkaConfig), 1);

		topology.setBolt("MultiplexerBolt", new MultiplexerBolt(), 1).shuffleGrouping("KafkaSpout");
		//topology.setBolt("MultiplexerBolt", new MultiplexerBolt(), 1).shuffleGrouping("NettySpout");
		topology.setBolt("NetworkDataBolt", new NetworkDataBolt("NetworkData"), 5).shuffleGrouping("MultiplexerBolt", "NetworkDataStream");
		topology.setBolt("LogMatchesBolt", new LogMatchesBolt("LogMatches"), 1).fieldsGrouping("NetworkDataBolt", new Fields("matches"));
//		topology.setBolt("MemUsageRollingCountBolt", new RollingCountBolt("MemUsage",30,1)).shuffleGrouping("MultiplexerBolt", "MemUsageStream");
//		topology.setBolt("MemUsageAnalyser", new UsageAnalyser("MemUsage")).fieldsGrouping("MemUsageRollingCountBolt", new Fields("hostname","count"));
//		topology.setBolt("CpuUsageRollingCountBolt", new RollingCountBolt("CpuUsage",30,1)).shuffleGrouping("MultiplexerBolt", "CpuUsageStream");
//		topology.setBolt("CpuUsageAnalyser", new UsageAnalyser("CpuUsage")).fieldsGrouping("CpuUsageRollingCountBolt", new Fields("hostname","count"));
//		topology.setBolt("FileSystemUsageRollingCountBolt", new RollingCountBolt("FileSystemUsage",30,1)).shuffleGrouping("MultiplexerBolt", "FileSystemUsageStream");
//		topology.setBolt("FileSystemUsageAnalyser", new UsageAnalyser("FileSystemUsage")).fieldsGrouping("FileSystemUsageRollingCountBolt", new Fields("hostname","count"));
		topology.setBolt("PrettyPrinterBolt", new PrettyPrinterBolt(), 1).shuffleGrouping("MultiplexerBolt", "DefaultStream");
		return topology;
	}
}
