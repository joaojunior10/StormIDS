package networkmonitor.bolts.networkdata;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.topology.base.BaseRichBolt;
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
import java.util.Map;

public class NetworkDataBolt extends BaseRichBolt {
	private Matcher matcher;
    private OutputCollector collector;
    private static final long serialVersionUID = 1L;
	String topic;
	private static final Logger LOG = LoggerFactory.getLogger(NetworkDataBolt.class);

    public NetworkDataBolt(String topic) {

    }

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        Rules rules = new Rules();
        try {
            matcher = new Matcher(rules.get());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            LOG.error("Error reading rules - " + e.getStackTrace());
        }
        this.collector = collector;
        LOG.info("NetworkDataBolt initiated");
    }

    @Override
    public void execute(Tuple input) {
        LOG.info("Packets received");
        String jsonObj = (String) input.getValue(0);
        treatData(jsonObj,  collector);
    }

    public void treatData(String jsonObj, OutputCollector collector) {
		JsonParser parser = new JsonParser();
		JsonObject obj = parser.parse(jsonObj).getAsJsonObject();
        String hostname = obj.get("hostname").getAsString();
		//Match packets
		Type listType = new TypeToken<List<PacketData>>() {}.getType();

		List<PacketData> packets = new Gson().fromJson(obj.getAsJsonArray("packetList"),listType);
		LOG.info("Packets: " + packets.size());

		this.matcher.match(packets, hostname);
        //Send result to LogMatchesBolt
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        if(this.matcher.matches.size() > 0) {
			LOG.info("Matches sent: " + this.matcher.matches.size());
			String matches = gson.toJson(this.matcher.matches);
            //reset Matches
			if(collector != null)
            	collector.emit(new Values(matches));
        }
	}

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("matches"));
    }
}