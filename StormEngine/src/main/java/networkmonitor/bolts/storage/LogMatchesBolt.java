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
    private int total = 0;
    public LogMatchesBolt(String topic) {

    }

    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector){
        context.getThisTaskId();
        this.collector = collector;
        Cluster cluster = Cluster.builder().addContactPoint(stormConf.get("cassandra.address").toString()).build();
        session = cluster.connect(stormConf.get("cassandra.keyspace").toString());
        statement = session.prepare("INSERT INTO matches (id, timelog, hostname, " +
                "sourceip, destinationip, sourceport, destinationport, " +
                "msg, action, rule, packet) VALUES (?,?,?,?,?,?,?,?,?,?,?)");
    }

    public void execute(Tuple input) {
        saveMatches(input);
    }

    private void saveMatches(Tuple input) {

        List<Match> matches = (List<Match>) input.getValue(0);
        int size = matches.size();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        List<ResultSetFuture> futures = new ArrayList<ResultSetFuture>();
        for (int i = 0; i < size ; i++) {
            Match match = matches.get(i);
            BoundStatement bind = statement.bind(UUID.randomUUID(), GregorianCalendar.getInstance().getTime(), match.hostname,
                    match.sourceIP, match.destinationIP, match.sourcePort, match.destinationPort,
                    match.msg, match.action, match.rule, match.packet);
            ResultSetFuture resultSetFuture = session.executeAsync(bind);
            futures.add(resultSetFuture);
            total++;
        }
        for(ResultSetFuture future: futures){
            future.getUninterruptibly();
        }
        LOG.info("Matches Saved: " + size +" - "+ sdf.format(System.currentTimeMillis()));
        LOG.info("Total: " + total);
        this.collector.ack(input);
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("id", "date","hostname"));
    }
}
