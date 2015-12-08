package networkmonitor.bolts;


import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import util.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PrettyPrinterBolt extends BaseBasicBolt {

	/**
	 * 
	 */
	Map<String, List<JSONObject>> monitoredDevices = new HashMap<String, List<JSONObject>>();
	private static final long serialVersionUID = 1L;

	public void execute(Tuple tuple, BasicOutputCollector collector) {
		try {
			
			JSONObject jsonObj = (JSONObject) tuple.getValue(0);
			String hostname = jsonObj.getString("hostname");
			String topic = jsonObj.getString("topic");

			List<JSONObject> listOfTopicsOfaHostname = monitoredDevices.get(hostname);
			if(listOfTopicsOfaHostname != null){
				for(JSONObject item : listOfTopicsOfaHostname){
					if(item.getString("topic").equals(topic)){
						listOfTopicsOfaHostname.remove(item);
						break;
					}
				}
				listOfTopicsOfaHostname.add(jsonObj);
			}
			else{
				listOfTopicsOfaHostname = new ArrayList<JSONObject>();
				listOfTopicsOfaHostname.add(jsonObj);
				monitoredDevices.put(hostname,listOfTopicsOfaHostname );
			}
			System.out.println(listOfTopicsOfaHostname);
			writeOutputToFile( hostname, listOfTopicsOfaHostname);


		} catch (Exception e) {
			e.printStackTrace();
			}
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("Topic", "JsonObject" ) );
	}

	private void writeOutputToFile(String hostname, List<JSONObject> list) {
		PrintWriter writer = null;

		try {
			File file = new File("./analytics/"
					+ hostname + ".json");
			file.getParentFile().mkdirs();
			writer = new PrintWriter(file);
			for(JSONObject item : list){
				writer.println(item.toString(3) + "\n");
			}


		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				writer.close();
			}
		}

	}
}
