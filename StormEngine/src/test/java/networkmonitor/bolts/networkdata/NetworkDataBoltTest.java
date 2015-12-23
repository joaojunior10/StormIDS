package networkmonitor.bolts.networkdata;

import backtype.storm.tuple.Tuple;
import networkmonitor.bolts.MockTupleHelpers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by joao on 9/6/15.
 */
public class NetworkDataBoltTest {
    private NetworkDataBolt networkdata;
    private long start;
    @Before
    public void setup(){
        networkdata = new NetworkDataBolt("networkdata");
        start = System.currentTimeMillis();
    }
    @Test
    public void testNetworkData() throws Exception {

        String packets = "{\"hostname\":\"Joaos-MacBook-Pro.local\",\"packetList\":[{\"TTL\":43,\"ack\":1553217724,\"data\":\"uOhWPQk4bHCf1cIeCABFAABQ1N4AACsGtHxA6bpfCgABBQG7weeTnTDYXJQ4vPAQD/CeTQAAAQEICjDYRF0o1s5UAQEFGlyUp4RclQJgXJSAvlyUofpclE7kXJR7NA==\"," +
                "\"destinationIP\":\"10.0.1.5\",\"destinationPort\":\"49639\",\"dsize\":80,\"flags\":{},\"flow\":{},\"fragbits\":{\"D\":false,\"M\":false,\"R\":false},\"fragoffset\":0,\"icmp\":0,\"icmp_id\":0,\"icmp_seq\":0,\"icode\":0,\"id\":54494," +
                "\"ip_proto_int\":0,\"ipopts\":{},\"protocol\":\"TCP\",\"sameip\":0,\"seq\":-1818414888,\"sourceIP\":\"64.233.186.95\",\"sourcePort\":\"443\",\"tos\":0,\"window\":4080}],\"topic\":\"NetworkData\"}";
        Tuple input = MockTupleHelpers.mockTickTuple(packets);
        networkdata.execute(input);
    }
    @After
    public void end() {
        System.out.println(System.currentTimeMillis() - start);
    }
}

