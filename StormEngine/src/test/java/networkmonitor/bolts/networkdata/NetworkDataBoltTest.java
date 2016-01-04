package networkmonitor.bolts.networkdata;

import backtype.storm.Config;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.tuple.Tuple;
import networkmonitor.bolts.MockOutputCollector;
import networkmonitor.bolts.MockTopologyContext;
import networkmonitor.bolts.MockTuple;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import util.Response;
import util.packetdata.PacketData;

import java.util.*;

/**
 * Created by joao on 9/6/15.
 */
public class NetworkDataBoltTest {
    private NetworkDataBolt networkdata;
    private long start;
    @Before
    public void setup(){
        networkdata = new NetworkDataBolt("networkdata");
        Config config = new Config();
        TopologyContext context = MockTopologyContext.mockTopologyContext();
        OutputCollector collector = MockOutputCollector.mockOutputCollector();
        networkdata.prepare(config,context,collector);
        start = System.currentTimeMillis();
    }
    @Test
    public void testNetworkData() throws Exception {

        PacketData packetData = new PacketData();
        packetData.data = GetData();
        packetData.protocol = "tcp";
        packetData.sourceIP = "10.1.1.114";
        packetData.sourcePort = "3000";
        packetData.destinationIP = "10.1.1.32";
        packetData.destinationPort = "3000";
        List<PacketData> packets = new ArrayList<>();
        for (int i = 0; i < 10000 ; i++) {
            packets.add(packetData);
        }
        Response response = new Response(packets,"Joaos-MacBook-Pro.local","NetworkData");
        Tuple input = MockTuple.mockTickTuple(response);
        long startTime = System.currentTimeMillis();
        networkdata.execute(input);
        long endTime = System.currentTimeMillis();
        long duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.
        System.out.println("Duração: "+duration);
    }

    private byte[] GetData() {
        Random random = new Random();
        byte[] buffer = new byte[256];
        random.nextBytes(buffer);
        byte[] msg = ("StormIDS Test").getBytes();
        for (int j = 0; j < msg.length; j++){
            buffer[j] =  msg[j];
        }
        return buffer;
    }

    @After
    public void end() {
        System.out.println(System.currentTimeMillis() - start);
    }
}

