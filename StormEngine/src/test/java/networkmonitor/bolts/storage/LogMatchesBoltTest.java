package networkmonitor.bolts.storage;


import backtype.storm.Config;
import backtype.storm.Constants;
import backtype.storm.tuple.Tuple;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.google.gson.Gson;
import networkmonitor.bolts.MockTupleHelpers;
import org.junit.Test;
import util.matcher.Match;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by joao on 9/6/15.
 */
public class LogMatchesBoltTest {

    @Test
    public void testSaveMatch() throws Exception {
        Match match = new Match();
        match.hostname="PC-Joao";
        match.sourceIP="10.0.1.5";
        match.destinationIP = "186.213.210.125";
        match.sourcePort="8080";
        match.destinationPort="4321";

        LogMatchesBolt logMatchesBolt = new LogMatchesBolt("networkdata");
        Config config = new Config();
        config.setDebug(true);
        config.put("cassandra.keyspace","stormids");
        config.put("cassandra.address","localhost");
        logMatchesBolt.prepare(config,null,null);
        Gson gson = new Gson();
        List<Match> list = new ArrayList<>();
        list.add(match);
        String matches = gson.toJson(list);
        Tuple input = MockTupleHelpers.mockTickTuple(matches);
        logMatchesBolt.execute(input);

        Cluster cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
        Session session = cluster.connect("stormids");

        ResultSet results = session.execute("SELECT * FROM matches");
        for (Row row : results) {
            System.out.format("%s %s %s\n\n", row.getUUID("id"), row.getTimestamp("timelog"),  row.getString("hostname"));
        }
    }
}

