
/*
 * Netty Topology
 * Queueless (No Kafka like queue) Topology to monitor and rank the load of network assets
 */
package networkmonitor.topology;

import networkmonitor.bolts.MultiplexerBolt;
import networkmonitor.bolts.PrettyPrinterBolt;
import networkmonitor.bolts.analyser.UsageAnalyser;
import networkmonitor.bolts.networkdata.NetworkDataBolt;
import networkmonitor.bolts.networkflow.NetworkFlowBolt;
import networkmonitor.bolts.slidingwindowcounter.RollingCountBolt;
import networkmonitor.spouts.netty.NettySpout;
import networkmonitor.spouts.snortrules.SnortRulesSpout;
import util.storm.StormRunner;
import backtype.storm.Config;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;

public class NettyTopology{

	final public static Integer nettyServerPort = 8992;

	public static void main(String[] args) {

		TopologyBuilder builder = createTopology();

		Config config = new Config();
		config.setDebug(true);
		//config.setMaxSpoutPending(1);
		try {
			StormRunner.runTopologyLocally(builder.createTopology(),
					"NetworkMonitor - Netty Topology", config, 0);
		} catch (InterruptedException e) {
			System.out.println("\n\n Execution interrupted. \n\n");
		}
	}

	static private TopologyBuilder createTopology() {

		TopologyBuilder topology = new TopologyBuilder();
		// One single Netty spout listening for clients connections
		topology.setSpout("NettySpout", new NettySpout(nettyServerPort), 1);
		// Various (2) fetcher bolts -> shuffle grouping from feed spout
		//topology.setBolt("PrettyPrinterBolt", new PrettyPrinterBolt(), 1).shuffleGrouping("NettySpout");

		topology.setBolt("MultiplexerBolt", new MultiplexerBolt(), 1).shuffleGrouping("NettySpout");
//		topology.setBolt("NetworkFlowBolt", new NetworkFlowBolt("NetworkFlow")).shuffleGrouping("MultiplexerBolt","NetworkFlowStream");
		topology.setBolt("NetworkDataBolt", new NetworkDataBolt("NetworkData")).shuffleGrouping("MultiplexerBolt","NetworkDataStream");
//		topology.setBolt("MemUsageRollingCountBolt", new RollingCountBolt("MemUsage",30,1)).shuffleGrouping("MultiplexerBolt", "MemUsageStream");
//		topology.setBolt("MemUsageAnalyser", new UsageAnalyser("MemUsage")).fieldsGrouping("MemUsageRollingCountBolt", new Fields("hostname","count"));
//		topology.setBolt("CpuUsageRollingCountBolt", new RollingCountBolt("CpuUsage",30,1)).shuffleGrouping("MultiplexerBolt", "CpuUsageStream");
//		topology.setBolt("CpuUsageAnalyser", new UsageAnalyser("CpuUsage")).fieldsGrouping("CpuUsageRollingCountBolt", new Fields("hostname","count"));
//		topology.setBolt("FileSystemUsageRollingCountBolt", new RollingCountBolt("FileSystemUsage",30,1)).shuffleGrouping("MultiplexerBolt", "FileSystemUsageStream");
//		topology.setBolt("FileSystemUsageAnalyser", new UsageAnalyser("FileSystemUsage")).fieldsGrouping("FileSystemUsageRollingCountBolt", new Fields("hostname","count"));

		topology.setBolt("PrettyPrinterBolt", new PrettyPrinterBolt(), 1).shuffleGrouping("MultiplexerBolt","DefaultStream");

		return topology;
	}





}
