package networkmonitor.bolts.storage;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

import util.json.JSONArray;
import util.json.JSONObject;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.UUID;


/**
 * Created by joao on 9/6/15.
 */
public class LogMatchesBolt extends BaseRichBolt{
    private OutputCollector _collector;
    private Session _session;
    public LogMatchesBolt(String topic) {

    }
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector){
        _collector = collector;
        Cluster cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
        _session = cluster.connect("stormids");
    }

    public void execute(Tuple input) {
        JSONArray matches = (JSONArray) input.getValue(0);
        for (int i = 0; i < matches.length() ; i++) {
            JSONObject match = matches.getJSONObject(i);
            String timelog = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());

            // Insert one record into the users table
            _session.execute("INSERT INTO test (id, date, hostname) VALUES ("+UUID.randomUUID()+", dateOf(now()), '"+match.get("hostname")+"')");

            //_collector.emit(input, new Values(UUID.randomUUID(), timelog, match.get("hostname")));
        }
    }

    public void treatData(JSONObject jsonObj, BasicOutputCollector collector) {
    }

    public void saveMatch(JSONArray matches){

    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("id", "date","hostname"));

    }
}
