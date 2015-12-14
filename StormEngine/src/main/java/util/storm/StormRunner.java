package util.storm;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.generated.StormTopology;

public final class StormRunner {

	private static final int MILLIS_IN_SEC = 1000;

	private StormRunner() {
	}

	public static void runTopologyLocally(StormTopology topology,
			String topologyName, Config conf, int runtimeInSeconds)
			throws InterruptedException {
		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology(topologyName, conf, topology);
		System.out
				.println("\n\n==================================\n STORM TOPOLOGY INITIALIZING \n==================================\n\n");
		// If the runtime is 0, it will run indefinitely
		if (runtimeInSeconds != 0) {
			Thread.sleep((long) runtimeInSeconds * MILLIS_IN_SEC);
			cluster.killTopology(topologyName);
			cluster.shutdown();
		}
	}
}
