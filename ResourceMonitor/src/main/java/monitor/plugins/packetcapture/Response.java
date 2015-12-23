package monitor.plugins.packetcapture;

import monitor.plugins.packetcapture.packetdata.PacketData;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jjunior on 23/12/2015.
 */
public class Response implements Serializable{
    List<PacketData> packetData;
    String hostname;
    String topic;

    public Response(List<PacketData> packetData,String hostname,String topic){
        this.packetData = packetData;
        this.hostname = hostname;
        this.topic = topic;
    }
}
