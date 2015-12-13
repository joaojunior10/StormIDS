package networkmonitor.bolts.storage;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import com.datastax.driver.core.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.matcher.Match;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Created by joao on 9/6/15.
 */
public class LogMatchesBolt extends BaseRichBolt{
    private OutputCollector collector;
    private Session session;
    private static final Logger LOG = LoggerFactory.getLogger(LogMatchesBolt.class);
    private PreparedStatement statement;
    public LogMatchesBolt(String topic) {

    }

    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector){
        collector = collector;
        Cluster cluster = Cluster.builder().addContactPoint(stormConf.get("cassandra.address").toString()).build();
        session = cluster.connect(stormConf.get("cassandra.keyspace").toString());
        statement = session.prepare("INSERT INTO matches (id, timelog, hostname, " +
                "sourceip, destinationip, sourceport, destinationport, " +
                "msg, action, rule, packet) VALUES (?,?,?,?,?,?,?,?,?,?,?)");
    }

    public void execute(Tuple input) {
        LOG.info("Matches received");
        saveMatches(input);
    }

    private void saveMatches(Tuple input) {
        Type listType = new TypeToken<List<Match>>() {}.getType();
        String json = (String) input.getValue(0);
        List<Match> matches =  new Gson().fromJson(json,listType);
        LOG.info("Matches received: " + matches.size());

        //Save off the prepared statement you're going to use


//        List<ResultSetFuture> futures = new ArrayList<ResultSetFuture>();

        for (int i = 0; i < matches.size() ; i++) {
            Match match = matches.get(i);

            BoundStatement bind = statement.bind(UUID.randomUUID(), GregorianCalendar.getInstance().getTime(), match.hostname,
                    match.sourceIP, match.destinationIP, match.sourcePort, match.destinationPort,
                    match.msg, match.action, match.rule, match.packet);
            ResultSetFuture resultSetFuture = session.executeAsync(bind);
//            futures.add(resultSetFuture);
        }
        //not returning anything useful but makes sure everything has completed before you exit the thread.
//        for(ResultSetFuture future: futures){
//            future.getUninterruptibly();
//        }
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("id", "date","hostname"));
    }
}
