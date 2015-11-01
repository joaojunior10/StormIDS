package networkmonitor.bolts.storage;


import org.junit.Test;
import util.matcher.Match;
import util.rules.GeneralOptions;

import static org.junit.Assert.*;

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
        match.generalOptions = new GeneralOptions();
        match.generalOptions.msg = "test";
        match.generalOptions.sid = 132;

        LogMatchesBolt logMatchesBolt = new LogMatchesBolt("networkdata");

    }
}