package networkmonitor.bolts.networkdata;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.json.JSONArray;
import util.json.JSONObject;
import util.matcher.Matcher;
import util.rules.Rules;

import java.util.ArrayList;
import java.util.List;

public class NetworkDataBolt extends BaseBasicBolt {
	private Matcher matcher;
	private static final long serialVersionUID = 1L;
	String topic;
	private static final Logger LOG = LoggerFactory.getLogger(NetworkDataBolt.class);

    public NetworkDataBolt(String topic) {
        Rules rules = new Rules();
        matcher = new Matcher(rules.get());
        LOG.info("NetworkDataBolt initiated");
    }

    public void treatData(JSONObject jsonObj, BasicOutputCollector collector) {
        String hostname = jsonObj.getString("hostname");
		//Match packets
		List<PacketData> packets = parsePacket(jsonObj);
        this.matcher.match(packets, hostname);
        //Send result to LogMatchesBolt
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        if(this.matcher.matches.size() > 0) {
            String matches = gson.toJson(this.matcher.matches);
            collector.emit(new Values(matches));
        }
	}
	//TODO user GSON
	public List<PacketData> parsePacket(JSONObject jsonObjList) {
		JSONArray jsonPackets = jsonObjList.optJSONArray("packetList");
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

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("matches"));
    }
}