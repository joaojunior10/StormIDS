package networkmonitor.bolts.analyser;

import util.json.JSONObject;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;

public abstract class Analyser  extends BaseBasicBolt{

	
	/**
	 * Prototype class for bolts that analyze very frequent data.
	 */
	private static final long serialVersionUID = 1L;
	String topic;
	
	//Every bolt that implements this class must decide how they want to treat the data.
	public abstract void treatData(JSONObject jsonObj,  BasicOutputCollector collector);
	
	public Analyser(String topic){
		this.topic = topic;
	}

	public void execute(Tuple input, BasicOutputCollector collector) {
		JSONObject jsonObj;
		jsonObj = (JSONObject) input.getValue(0);
		treatData(jsonObj,  collector);
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declareStream(getTopic()+"Stream",new Fields("JsonObject"));
	}

	String getTopic(){
		return topic;
	}
}
