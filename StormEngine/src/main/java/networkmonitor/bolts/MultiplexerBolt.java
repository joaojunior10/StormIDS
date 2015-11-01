package networkmonitor.bolts;


import util.json.JSONObject;
import util.json.JSONTokener;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class MultiplexerBolt extends BaseBasicBolt {

	/**
	 * MultiplexerBolt 
	 * Distributes the computation based on the topic.
	 */
	private static final long serialVersionUID = 1L;
	enum topics{
		//NetRouteList, ProcessList
		MemUsage,
		CpuUsage,
		FileSystemUsage,
		NetworkFlow,
		NetworkData
	}

	public void execute(Tuple tuple, BasicOutputCollector collector) {
		try {
			//Parse Json Object
			JSONObject jsonObj = new JSONObject(new JSONTokener((String) tuple.getValue(0)));
			String topic = jsonObj.getString("topic");
			
			//collector.emit(new Values(topic, jsonObj));
			boolean sent = false;
			//TODO refactoring - use a switch case or pattern matching
			//TODO divide packet in 8 streams
			for (topics tc : topics.values()){
				if (topic.equals(tc.name())) {
					collector.emit(topic+"Stream",new Values(jsonObj));
					sent = true;
				}
			}
			if(!sent) collector.emit("DefaultStream",new Values(jsonObj));
		} catch (Exception e) {
			e.printStackTrace();
		}
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
