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
        if(!match) return match;
        match &= header.ipsSrc.containsKey("any") || header.ipsSrc.containsKey(packet.sourceIP);
        if(!match) return match;
        match &= header.ipsDst.containsKey("any") || header.ipsDst.containsKey(packet.destinationIP);
        if(!match) return match;
        match &= header.portsSrc.containsKey("any") || header.portsSrc.containsKey(packet.sourcePort);
        if(!match) return match;
        match &= header.portsDst.containsKey("any") || header.portsDst.containsKey(packet.destinationPort);
        if(!match) return match;
        if(header.direction == 2 && !match){
            match = true;
            match &= header.ipsSrc.containsKey("any") || header.ipsSrc.containsKey(packet.destinationIP);
            if(!match) return match;
            match &= header.ipsDst.containsKey("any") || header.ipsDst.containsKey(packet.sourceIP);
            if(!match) return match;
            match &= header.portsSrc.containsKey("any") || header.portsSrc.containsKey(packet.destinationPort);
            if(!match) return match;
            match &= header.portsDst.containsKey("any") || header.portsDst.containsKey(packet.sourcePort);
            if(!match) return match;
        }
        return match;
    }
}
