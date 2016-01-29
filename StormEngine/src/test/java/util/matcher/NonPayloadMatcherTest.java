package util.matcher;

import org.junit.Test;
import util.packetdata.PacketData;
import util.rules.Operators;
import util.rules.nonpayload.Fragbits;
import util.rules.nonpayload.NonPayloadOptions;

import java.util.HashMap;

/**
 * Created by joao on 9/6/15.
 */
public class NonPayloadMatcherTest {

    @Test
    public void matchNonPayload() throws Exception {
        NonPayloadOptions nonPayloadOptions = new NonPayloadOptions();
        nonPayloadOptions.fragbits = new Fragbits();
        nonPayloadOptions.fragbits.M = true;
        nonPayloadOptions.fragoffset.fragoffset = 0;
        nonPayloadOptions.ttl.max = 5;
        nonPayloadOptions.ttl.operation = Operators.GREATEROREQUALTHAN;
        nonPayloadOptions.tos.tos = 4;
        nonPayloadOptions.id = 31337;
        nonPayloadOptions.ipopts = "lsrr";
        nonPayloadOptions.fragbits.M = true;
        nonPayloadOptions.fragbits.D = true;
        nonPayloadOptions.fragbits.operation = Operators.MATCH;
        nonPayloadOptions.flags.flags.add("SF");
        nonPayloadOptions.flags.flags.add("CE");

        NonPayloadMatcher nonPayloadMatcher = new NonPayloadMatcher();

        PacketData packetData = new PacketData();
        packetData.fragbits.M = true;
        packetData.fragbits.D = true;
        packetData.TTL = 64;
        packetData.fragoffset = 0;
        packetData.id = 31337;
        packetData.ipopts = new HashMap<>();
        packetData.ipopts.put("lsrr",1);
        nonPayloadMatcher.match(packetData,nonPayloadOptions);

    }
}