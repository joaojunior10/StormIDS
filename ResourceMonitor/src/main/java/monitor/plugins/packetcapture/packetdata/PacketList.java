package monitor.plugins.packetcapture.packetdata;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joao on 7/12/15.
 */
public class PacketList {
    public List<PacketData> packetList;

    public PacketList(){
        this.packetList = new ArrayList<PacketData>();
    }
}
