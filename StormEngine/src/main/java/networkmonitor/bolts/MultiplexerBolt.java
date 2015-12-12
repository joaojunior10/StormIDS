package networkmonitor.bolts;


import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import util.json.JSONObject;
import util.json.JSONTokener;

public class MultiplexerBolt extends BaseBasicBolt {

	/**
	 * MultiplexerBolt 
	 * Distributes the computation based on the topic.
	 */
	private static final long serialVersionUID = 1L;
	enum topics{
		//NetRouteList, ProcessList
		NetworkData,
		MemUsage,
		CpuUsage,
		FileSystemUsage,
		NetworkFlow,

	}

	public void execute(Tuple tuple, BasicOutputCollector collector) {
		try {
			//Parse Json Object
			String jsonObj = (String) tuple.getValue(0);
			JsonParser parser = new JsonParser();
			JsonObject obj = parser.parse(jsonObj).getAsJsonObject();
			String topic = obj.get("topic").getAsString();
			boolean sent = false;
			//TODO refactoring - use a switch case or pattern matching
			//TODO divide packet in 8 streams
			for (topics tc : topics.values()){
				if (topic.equals(tc.name())) {
					collector.emit(topic+"Stream",new Values(jsonObj));
					sent = true;
					break;
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
