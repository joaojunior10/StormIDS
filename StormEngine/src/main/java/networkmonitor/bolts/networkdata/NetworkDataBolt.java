package networkmonitor.bolts.networkdata;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Response;
import util.matcher.Matcher;
import util.packetdata.PacketData;
import util.rules.Rules;
import util.rules.SnortSignature;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
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
        Response response = (Response) input.getValue(0);
        treatData(response,  collector);
        this.collector.ack(input);
    }

    public void treatData(Response response, OutputCollector collector) {
        String hostname = response.hostname;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

		List<PacketData> packets = response.packetData;
        //LOG.info(taskId+" - Packets received: " + packets.size() +" - "+ sdf.format(System.currentTimeMillis()));
        //Match packets
        Matcher matcher = new Matcher(this.rules);
        matcher.match(packets, hostname);
        //Send result to LogMatchesBolt

        if(matcher.matches.size() > 0) {
            //reset Matches
			if(collector != null)
            	collector.emit(new Values(matcher.matches));
        }
       // LOG.info(taskId + " - Packets processed: " + packets.size() +" - "+ sdf.format(System.currentTimeMillis()));
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("matches"));
    }
}