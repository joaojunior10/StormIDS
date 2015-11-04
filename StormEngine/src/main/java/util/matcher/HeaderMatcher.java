package util.matcher;

import networkmonitor.bolts.networkdata.PacketData;
import util.rules.SnortSignature;

/**
 * Created by joao on 2/11/15.
 */
public class HeaderMatcher {
    public static boolean match(PacketData packet, SnortSignature rule) {
        boolean match = true;
        match &= rule.header.protocol.equals(packet.protocol);
        match &= rule.header.ipsSrc.containsKey("any") || rule.header.ipsSrc.containsKey(packet.sourceIP);
        match &= rule.header.ipsDst.containsKey("any") || rule.header.ipsDst.containsKey(packet.destinationIP);
        match &= rule.header.portsSrc.containsKey("any") || rule.header.portsSrc.containsKey(packet.sourcePort);
        match &= rule.header.portsDst.containsKey("any") || rule.header.portsDst.containsKey(packet.destinationPort);
        if(rule.header.direction == 2 && !match){
            match = true;
            match &= rule.header.ipsSrc.containsKey("any") || rule.header.ipsSrc.containsKey(packet.destinationIP);
            match &= rule.header.ipsDst.containsKey("any") || rule.header.ipsDst.containsKey(packet.sourceIP);
            match &= rule.header.portsSrc.containsKey("any") || rule.header.portsSrc.containsKey(packet.destinationPort);
            match &= rule.header.portsDst.containsKey("any") || rule.header.portsDst.containsKey(packet.sourcePort);
        }
        return match;
    }
}
