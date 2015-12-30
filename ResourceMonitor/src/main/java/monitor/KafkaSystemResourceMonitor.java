package monitor;

import java.util.Properties;

import monitor.connectors.SystemResourceMonitor;
import monitor.connectors.kafka.KafkaChannelSpecification;
import monitor.plugins.*;
import util.Response;
import monitor.plugins.prototype.SystemResourcePlugin;
import kafka.javaapi.producer.Producer;
import kafka.producer.ProducerConfig;


public class KafkaSystemResourceMonitor extends SystemResourceMonitor {

	public KafkaSystemResourceMonitor(String host, int port) {
		super(host, port);
	}

	public static void main(String[] args) {
		//Creates the monitor

		KafkaSystemResourceMonitor monitor = new KafkaSystemResourceMonitor("127.0.0.1", 2181);

		//Add the plugins

//		CpuUsagePlugin plugin1 = new CpuUsagePlugin(DEFAULT_SHORT_PROBE_TIME_INTERVAL);
//		MemUsagePlugin plugin2 = new MemUsagePlugin(DEFAULT_SHORT_PROBE_TIME_INTERVAL);
//		FileSystemUsagePlugin plugin3 = new FileSystemUsagePlugin(DEFAULT_SHORT_PROBE_TIME_INTERVAL);
//		NetInfoPlugin plugin4 = new NetInfoPlugin(DEFAULT_LONG_PROBE_TIME_INTERVAL);
//		CpuInfoPlugin plugin5 = new CpuInfoPlugin(DEFAULT_LONG_PROBE_TIME_INTERVAL);
//		MemInfoPlugin plugin6 = new MemInfoPlugin(DEFAULT_LONG_PROBE_TIME_INTERVAL);
//		NetRouteListPlugin plugin7 = new NetRouteListPlugin(DEFAULT_LONG_PROBE_TIME_INTERVAL);
//		NetInterfaceStatsPlugin plugin8 = new NetInterfaceStatsPlugin(DEFAULT_LONG_PROBE_TIME_INTERVAL);
//		ProcessListPlugin plugin9 = new ProcessListPlugin(DEFAULT_LONG_PROBE_TIME_INTERVAL);
//		PacketCapturePlugin plugin10 = new PacketCapturePlugin(DEFAULT_SHORT_PROBE_TIME_INTERVAL);
		PacketDataPlugin plugin11 = new PacketDataPlugin(DEFAULT_SHORT_PROBE_TIME_INTERVAL);
//
//		monitor.addResourceMonitor(plugin1);
//		monitor.addResourceMonitor(plugin2);
//		monitor.addResourceMonitor(plugin3);
//		monitor.addResourceMonitor(plugin4);
//		monitor.addResourceMonitor(plugin5);
//		monitor.addResourceMonitor(plugin6);
//		monitor.addResourceMonitor(plugin7);
//		monitor.addResourceMonitor(plugin8);
//		monitor.addResourceMonitor(plugin10);
		monitor.addResourceMonitor(plugin11);


		//Run the monitor;
		monitor.run();

	}

	public void run() {

		Properties props = new Properties();
		props.put("metadata.broker.list", Config.getInstance().kafkaBroker);
		props.put("serializer.class", "util.ResponseEncoder");
		//props.put("partitioner.class", "example.producer.SimplePartitioner");
		props.put("request.required.acks", "1");
		props.put("zk.connect", Config.getInstance().kafkaZooKeeper);
		props.put("message.send.max.retries", new Integer(SystemResourceMonitor.DEFAULT_CHANNEL_CHECK_TIME_INTERVAL).toString());
		props.put("retry.backoff.ms", new Integer(SystemResourceMonitor.DEFAULT_CHANNEL_CHECK_TIME_INTERVAL).toString());
		ProducerConfig config = new ProducerConfig(props);

		System.out.println("[Resource Monitor] Monitoring your resources...");
		try {
			//Each monitor plugin is a thread, they manage how often they will send the info to the server.
			for(SystemResourcePlugin plugin : listOfResourceMonitors) {
				plugin.setChannel(new KafkaChannelSpecification (new Producer<String, Response>(config)));
				new Thread(plugin).start();
			}
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
		}	
	}
}





