package manager.controllers;

import com.datastax.driver.core.*;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.google.gson.Gson;
import manager.models.Match;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * Created by joao on 9/7/15.
 */
@RestController
public class MatchesController {
    @RequestMapping("/api/matches")
    public String matches() {
        //TODO paged search

        Cluster cluster = Cluster.builder().addContactPoint("10.1.1.121").build();
        Session session = cluster.connect("stormids");
        Gson gson = new Gson();
        ResultSet results = session.execute("SELECT * FROM matches");
        List<Match> matches = new ArrayList<>();
        for (Row row : results) {
            Match match = new Match();
            match.id = row.getUUID("id").toString();
            match.hostname = row.getString("hostname");
            match.timelog = row.getTimestamp("timelog");
            match.sourceIP = row.getString("sourceIP");
            match.destinationIP = row.getString("destinationIP");
            match.sourcePort = row.getString("sourcePort");
            match.destinationPort = row.getString("destinationPort");
            match.rule = row.getString("rule");
            match.packet = row.getString("packet");
            match.msg = row.getString("msg");
            matches.add(match);
        }

        matches.sort((o1, o2) -> o2.timelog.compareTo(o1.timelog));

        return gson.toJson(matches.subList(1,100));
    }
    @RequestMapping("/api/matchesByHost")
    public String matchesByHost(@RequestParam(value="hostname") String hostname) {

        return null;
    }

    @RequestMapping("/api/matchesByDate ")
     public String matchesByDate(@RequestParam(value="begin") String begin,@RequestParam(value="end") String end ) {

        return null;
    }
}
