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
        try {
            treatData(response, collector);
        }catch (Exception e) {
            e.printStackTrace();
            LOG.error("Error treating data - " + e.getStackTrace());
        }

        this.collector.ack(input);
    }

    public void treatData(Response response, OutputCollector collector) {
        String hostname = response.hostname;
		List<PacketData> packets = response.packetData;
        Matcher matcher = new Matcher(this.rules);
        matcher.match(packets, hostname);
        //Send result to LogMatchesBolt

        if(matcher.matches.size() > 0) {
            //reset Matches
			if(collector != null)
            	collector.emit(new Values(matcher.matches));
        }
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("matches"));
    }
}