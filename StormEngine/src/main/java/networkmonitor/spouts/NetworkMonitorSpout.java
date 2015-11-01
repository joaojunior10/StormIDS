package networkmonitor.spouts;

import java.util.Map;

import util.json.JSONObject;
import backtype.storm.spout.ISpout;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IComponent;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

/**
 * Prototype class for external spouts. Every external spout must extend this class
 * because it defines the output interface to the rest of the system.
 * */
public abstract class NetworkMonitorSpout extends BaseRichSpout implements ISpout, IComponent {
	private SpoutOutputCollector collector;
	
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("JsonUnparsed"));
		
	}
	
	public void setCollector (SpoutOutputCollector collector){
		this.collector = collector;
	}
	
	public void send(Values value){
		collector.emit(value);
	}
	
	/**
	 * You must call the setCollector method inside this method.
	 */
	public abstract void open(Map conf, TopologyContext context,
			SpoutOutputCollector collector);

	public abstract void nextTuple();

}
