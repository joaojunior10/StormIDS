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
import util.rules.SnortSignature;

import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NetworkDataBolt extends BaseRichBolt {
	private List<SnortSignature> rules;
    private OutputCollector collector;
    private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(NetworkDataBolt.class);
    private int taskId;

    public NetworkDataBolt(String topic) {

    }

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        Rules rules = new Rules();
        this.taskId = context.getThisTaskId();

        try {
            this.rules = rules.get();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            LOG.error("Error reading rules - " + e.getStackTrace());
        }
        this.collector = collector;
        LOG.info("NetworkDataBolt initiated");
    }

    @Override
    public void execute(Tuple input) {
        String jsonObj = (String) input.getValue(0);
        treatData(jsonObj,  collector);
        this.collector.ack(input);
    }

    public void treatData(String jsonObj, OutputCollector collector) {
		JsonParser parser = new JsonParser();
		JsonObject obj = parser.parse(jsonObj).getAsJsonObject();
        String hostname = obj.get("hostname").getAsString();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

		Type listType = new TypeToken<List<PacketData>>() {}.getType();

		List<PacketData> packets = new Gson().fromJson(obj.getAsJsonArray("packetList"),listType);
        LOG.info(taskId+" - Packets received: " + packets.size() +" - "+ sdf.format(System.currentTimeMillis()));
        //Match packets
        Matcher matcher = new Matcher(this.rules);
        matcher.match(packets, hostname);
        //Send result to LogMatchesBolt
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        if(matcher.matches.size() > 0) {
			String matches = gson.toJson(matcher.matches);
            //reset Matches
			if(collector != null)
            	collector.emit(new Values(matches));
        }
        LOG.info(taskId + " - Packets processed: " + packets.size() +" - "+ sdf.format(System.currentTimeMillis()));
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("matches"));
    }
}