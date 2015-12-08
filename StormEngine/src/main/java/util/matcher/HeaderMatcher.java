package util.matcher;

import networkmonitor.bolts.networkdata.PacketData;
import util.rules.header.Header;

/**
 * Created by joao on 2/11/15.
 */
public class HeaderMatcher {
    public static boolean match(PacketData packet, Header header) {
        boolean match = true;
        match &= header.protocol.equalsIgnoreCase(packet.protocol);
        match &= header.ipsSrc.containsKey("any") || header.ipsSrc.containsKey(packet.sourceIP);
        match &= header.ipsDst.containsKey("any") || header.ipsDst.containsKey(packet.destinationIP);
        match &= header.portsSrc.containsKey("any") || header.portsSrc.containsKey(packet.sourcePort);
        match &= header.portsDst.containsKey("any") || header.portsDst.containsKey(packet.destinationPort);
        if(header.direction == 2 && !match){
            match = true;
            match &= header.ipsSrc.containsKey("any") || header.ipsSrc.containsKey(packet.destinationIP);
            match &= header.ipsDst.containsKey("any") || header.ipsDst.containsKey(packet.sourceIP);
            match &= header.portsSrc.containsKey("any") || header.portsSrc.containsKey(packet.destinationPort);
            match &= header.portsDst.containsKey("any") || header.portsDst.containsKey(packet.sourcePort);
        }
        return match;
    }
}
