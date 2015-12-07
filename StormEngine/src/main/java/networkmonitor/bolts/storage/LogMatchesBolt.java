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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import util.json.JSONArray;
import util.json.JSONObject;
import util.matcher.Match;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * Created by joao on 9/6/15.
 */
public class LogMatchesBolt extends BaseRichBolt{
    private OutputCollector _collector;
    private Session _session;
    private Gson _gson;
    public LogMatchesBolt(String topic) {

    }
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector){
        _collector = collector;
        Cluster cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
        _session = cluster.connect("stormids");
        _gson = new Gson();
    }

    public void execute(Tuple input) {
        saveMatches(input);
    }

    private void saveMatches(Tuple input) {
        Type listType = new TypeToken<List<Match>>() {}.getType();
        String json = (String) input.getValue(0);
        List<Match> matches =  _gson.fromJson(json,listType);
        //TODO create bulk insert
        for (int i = 0; i < matches.size() ; i++) {
            Match match = matches.get(i);
            String query = "INSERT INTO matches" +
                    " (id, timelog, hostname, sourceip, destinationip, sourceport, destinationport" +
                    ",msg, action, rule, packet)" +
                    " VALUES ("+ UUID.randomUUID()+", dateOf(now()), " +
                    "'"+match.hostname+"', '"+match.sourceIP+"','"+match.destinationIP+"'" +
                    ",'"+match.sourcePort+"', '"+match.destinationPort+"', " +
                    "'"+match.msg+"', '"+match.action+"', '"+match.rule+"', '"+match.packet+"')";
            // Insert one record into the users table
            _session.execute(query);
        }
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("id", "date","hostname"));

    }
}
