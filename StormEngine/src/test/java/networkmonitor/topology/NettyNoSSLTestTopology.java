
/*
 * Netty Topology
 * Queueless (No Kafka like queue) Topology to monitor and rank the load of network assets
 */
package networkmonitor.topology;

import backtype.storm.Config;
import backtype.storm.topology.TopologyBuilder;
import networkmonitor.bolts.TesterBolt;
import networkmonitor.spouts.netty.NettySpoutNoSSL;
import util.storm.StormRunner;

public class NettyNoSSLTestTopology {

	final public static Integer nettyServerPort = 8992;

	public static void main(String[] args) throws Exception {

		TopologyBuilder builder = createTopology();

		Config config = new Config();
		config.setDebug(true);
		//config.setMaxSpoutPending(1);
		try {
			StormRunner.runTopologyLocally(builder.createTopology(),
					"NetworkMonitor - Netty No SSL Topology", config, 0);
		} catch (InterruptedException e) {
			System.out.println("\n\n Execution interrupted. \n\n");
		}
	}

	static private TopologyBuilder createTopology() {

		TopologyBuilder topology = new TopologyBuilder();
		// One single Netty spout listening for clients connections
		topology.setSpout("NettySpoutNoSSL", new NettySpoutNoSSL(nettyServerPort), 1);

		topology.setBolt("TesterBolt", new TesterBolt(), 1).shuffleGrouping("NettySpoutNoSSL");
			
		return topology;
	}





}
