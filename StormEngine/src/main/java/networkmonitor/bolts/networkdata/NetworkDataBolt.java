package networkmonitor.bolts.networkdata;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import networkmonitor.bolts.analyser.Analyser;
import networkmonitor.bolts.networkflow.NetworkFlowBolt;

import org.apache.log4j.Logger;

import util.json.JSONArray;
import util.json.JSONObject;
import util.json.JSONTokener;
import util.matcher.Match;
import util.matcher.Matcher;
import util.matcher.PayloadMatcher;
import util.rules.Rules;
import util.rules.SnortSignature;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.tuple.Tuple;

public class NetworkDataBolt extends BaseBasicBolt {
    private String hostname;
	private Matcher matcher;
	public NetworkDataBolt(String topic) {
        Rules rules = new Rules();
		matcher = new Matcher(rules.get());
	}

	/**
	 * UsageAnalyser
	 * Bolt that process very frequent data usage data from a resource.
	 */
	private static final long serialVersionUID = 1L;
	String topic;
	private static final Logger LOG = Logger.getLogger(NetworkFlowBolt.class);

	//Every bolt that implements this class must decide how they want to treat the data.
	public void treatData(JSONObject jsonObj, BasicOutputCollector collector) {
        hostname = jsonObj.getString("hostname");
		List<PacketData> packets = parsePacket(jsonObj);
        this.matcher.match(packets,hostname);
        //Send result to LogMatchesBolt
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        if(this.matcher.matches.size() > 0) {
            String json = gson.toJson(this.matcher.matches);
            JSONArray matches = new JSONArray(new JSONTokener(json));
            collector.emit(new Values(matches));
        }
	}




	public List<PacketData> parsePacket(JSONObject jsonObjList) {
		JSONArray jsonPackets = jsonObjList.optJSONArray("PacketData");
		List<PacketData> packets = new ArrayList<PacketData>();
		if(jsonPackets != null){
			for(int i = 0; i< jsonPackets.length(); i++){
				JSONObject jsonObj = jsonPackets.optJSONObject(i);
				PacketData packet = new PacketData();
				packet.data = jsonObj.optString("data");
				packet.sourceIP = jsonObj.optString("sourceIP");
				packet.destinationIP = jsonObj.optString("destinationIP");
				packet.sourcePort = jsonObj.optString("sourcePort");
				packet.destinationPort = jsonObj.optString("destinationPort");
				packet.fragoffset = jsonObj.optInt("fragoffset");
				packet.TTL = jsonObj.optInt("TTL");
				packet.tos = jsonObj.optInt("tos");
				packet.id = jsonObj.optInt("id");
				packet.dsize = jsonObj.optInt("dsize");
				
				packets.add(packet);
			}
			
		}
		return packets;
	}

	public void execute(Tuple input, BasicOutputCollector collector) {
		JSONObject jsonObj = (JSONObject) input.getValue(0);
		treatData(jsonObj,  collector);
	}

	void writeOutputToFile(String packet, String rule){
		String path = "./resources/analytics/NETWORK_DATA.txt";

		//creating file object from given path
		File file = new File(path);

		//FileWriter second argument is for append if its true than FileWritter will
		//write bytes at the end of File (append) rather than beginning of file
		FileWriter fileWriter;
		try {
			fileWriter = new FileWriter(file,true);
			//Use BufferedWriter instead of FileWriter for better performance
			BufferedWriter bufferFileWriter  = new BufferedWriter(fileWriter);
			fileWriter.append("Packet: " + packet +"\n");
			fileWriter.append("Rules: " + rule +"\n\n");

			//Don't forget to close Streams or Reader to free FileDescriptor associated with it
			bufferFileWriter.close();

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("matches"));
    }
}