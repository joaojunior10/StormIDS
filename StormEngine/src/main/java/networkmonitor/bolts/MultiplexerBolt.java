package networkmonitor.bolts;


import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import util.Response;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.util.Map;

public class MultiplexerBolt extends BaseRichBolt {

	/**
	 * MultiplexerBolt 
	 * Distributes the computation based on the topic.
	 */
	private static final long serialVersionUID = 1L;
	private OutputCollector collector;

	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
	}

	@Override
	public void execute(Tuple input) {
		try {
			Response response = (Response) input.getValue(0);
			String topic = response.topic;
			boolean sent = false;
			for (topics tc : topics.values()){
				if (topic.equals(tc.name())) {
					collector.emit(topic+"Stream",new Values(response));
					sent = true;
					break;
				}
			}
			if(!sent) collector.emit("DefaultStream",new Values(response));
			this.collector.ack(input);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	enum topics{
		//NetRouteList, ProcessList
		NetworkData,
		MemUsage,
		CpuUsage,
		FileSystemUsage,
		NetworkFlow,

	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		//declarer.declare(new Fields("Topic", "JsonObject" ) );
		declarer.declareStream("CpuUsageStream", new Fields("JsonObject" ) );
		declarer.declareStream("MemUsageStream", new Fields("JsonObject" ) );
		declarer.declareStream("FileSystemUsageStream", new Fields("JsonObject" ) );
		//declarer.declareStream("NetRouteListStream", new Fields("JsonObject" ) );
		//declarer.declareStream("ProcessListStream", new Fields("JsonObject" ) );
		declarer.declareStream("NetworkFlowStream", new Fields("JsonObject" ) );
		declarer.declareStream("NetworkDataStream", new Fields("JsonObject" ) );
		declarer.declareStream("DefaultStream", new Fields("JsonObject" ) );
	}
}
