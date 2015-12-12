package networkmonitor.bolts.networkdata;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.json.JSONArray;
import util.json.JSONObject;
import util.matcher.Match;
import util.matcher.Matcher;
import util.rules.Rules;

import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class NetworkDataBolt extends BaseBasicBolt {
	private Matcher matcher;
	private static final long serialVersionUID = 1L;
	String topic;
	private static final Logger LOG = LoggerFactory.getLogger(NetworkDataBolt.class);

    public NetworkDataBolt(String topic) {
        Rules rules = new Rules();
		try {
			matcher = new Matcher(rules.get());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		LOG.info("NetworkDataBolt initiated");
    }
	public void execute(Tuple input, BasicOutputCollector collector) {
		String jsonObj = (String) input.getValue(0);
		treatData(jsonObj,  collector);
	}

    public void treatData(String jsonObj, BasicOutputCollector collector) {
		JsonParser parser = new JsonParser();
		JsonObject obj = parser.parse(jsonObj).getAsJsonObject();
        String hostname = obj.get("hostname").getAsString();
		//Match packets
		Type listType = new TypeToken<List<PacketData>>() {}.getType();

		List<PacketData> packets = new Gson().fromJson(obj.getAsJsonArray("packetList"),listType);

		this.matcher.match(packets, hostname);
        //Send result to LogMatchesBolt
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        if(this.matcher.matches.size() > 0) {
			LOG.info("NetworkDataBolt");
			String matches = gson.toJson(this.matcher.matches);
			if(collector != null)
            	collector.emit(new Values(matches));
        }
	}
	//TODO user GSON
//	public List<PacketData> parsePacket(JSONObject jsonObjList) {
//		JSONArray jsonPackets = jsonObjList.optJSONArray("packetList");
//		List<PacketData> packets = new ArrayList<PacketData>();
//		if(jsonPackets != null){
//			for(int i = 0; i< jsonPackets.length(); i++){
//				JSONObject jsonObj = jsonPackets.optJSONObject(i);
//				PacketData packet = new PacketData();
//				packet.data = jsonObj.optString("data");
//				packet.protocol = jsonObj.optString("protocol");
//				packet.sourceIP = jsonObj.optString("sourceIP");
//				packet.destinationIP = jsonObj.optString("destinationIP");
//				packet.sourcePort = jsonObj.optString("sourcePort");
//				packet.destinationPort = jsonObj.optString("destinationPort");
//				packet.fragoffset = jsonObj.optInt("fragoffset");
//				packet.TTL = jsonObj.optInt("TTL");
//				packet.tos = jsonObj.optInt("tos");
//				packet.id = jsonObj.optInt("id");
//				packet.dsize = jsonObj.optInt("dsize");
//
//				packets.add(packet);
//			}
//
//		}
//		return packets;
//	}

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("matches"));
    }
}