package networkmonitor.topology;

import static com.github.fhuss.storm.cassandra.DynamicStatementBuilder.*;

import backtype.storm.task.OutputCollector;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import com.datastax.driver.core.exceptions.DriverException;
import com.github.fhuss.storm.cassandra.BaseExecutionResultHandler;
import com.github.fhuss.storm.cassandra.bolt.BatchCassandraWriterBolt;
import com.github.fhuss.storm.cassandra.bolt.CassandraWriterBolt;
import networkmonitor.bolts.MultiplexerBolt;
import networkmonitor.bolts.PrettyPrinterBolt;
import networkmonitor.bolts.analyser.UsageAnalyser;
import networkmonitor.bolts.networkdata.NetworkDataBolt;
import networkmonitor.bolts.slidingwindowcounter.RollingCountBolt;
import networkmonitor.bolts.storage.LogMatchesBolt;
import networkmonitor.spouts.kafka.KafkaSpout;
import storm.kafka.BrokerHosts;
import storm.kafka.SpoutConfig;
import storm.kafka.StringScheme;
import storm.kafka.ZkHosts;
import util.storm.StormRunner;
import backtype.storm.Config;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;

public class KafkaTopology{

	public static void main(String[] args) {

		TopologyBuilder builder = createTopology();

		Config config = new Config();
		config.setDebug(true);
		config.put("cassandra.keyspace","stormids");

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

		topology.setBolt("MultiplexerBolt", new MultiplexerBolt(), 1).shuffleGrouping("KafkaSpout");
		//topology.setBolt("MultiplexerBolt", new MultiplexerBolt(), 1).shuffleGrouping("NettySpout");
		topology.setBolt("NetworkDataBolt", new NetworkDataBolt("NetworkData")).shuffleGrouping("MultiplexerBolt", "NetworkDataStream");
		topology.setBolt("LogMatchesBolt", new LogMatchesBolt("LogMatches")).fieldsGrouping("NetworkDataBolt", new Fields("matches"));
//		topology.setBolt("CassandraWriterBolt", new CassandraWriterBolt(insertInto("test")
//                .values(
//                        with(fields("id", "date", "hostname")
//                        )).build()).withResultHandler(new EmitOnDriverExceptionResultHandler()));

//		topology.setBolt("MemUsageRollingCountBolt", new RollingCountBolt("MemUsage",30,1)).shuffleGrouping("MultiplexerBolt", "MemUsageStream");
//		topology.setBolt("MemUsageAnalyser", new UsageAnalyser("MemUsage")).fieldsGrouping("MemUsageRollingCountBolt", new Fields("hostname","count"));
//		topology.setBolt("CpuUsageRollingCountBolt", new RollingCountBolt("CpuUsage",30,1)).shuffleGrouping("MultiplexerBolt", "CpuUsageStream");
//		topology.setBolt("CpuUsageAnalyser", new UsageAnalyser("CpuUsage")).fieldsGrouping("CpuUsageRollingCountBolt", new Fields("hostname","count"));
//		topology.setBolt("FileSystemUsageRollingCountBolt", new RollingCountBolt("FileSystemUsage",30,1)).shuffleGrouping("MultiplexerBolt", "FileSystemUsageStream");
//		topology.setBolt("FileSystemUsageAnalyser", new UsageAnalyser("FileSystemUsage")).fieldsGrouping("FileSystemUsageRollingCountBolt", new Fields("hostname","count"));
//		
		topology.setBolt("PrettyPrinterBolt", new PrettyPrinterBolt(), 1).shuffleGrouping("MultiplexerBolt","DefaultStream");
		return topology;

	}
    public static class EmitOnDriverExceptionResultHandler extends BaseExecutionResultHandler {
        @Override
        protected void onDriverException(DriverException e, OutputCollector collector, Tuple tuple) {
            collector.emit("stream_error", new Values(e.getMessage()));
            collector.ack(tuple);
        }
    }
}
