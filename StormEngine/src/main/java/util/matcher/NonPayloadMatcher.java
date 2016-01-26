package util.matcher;

import util.packetdata.PacketData;
import util.rules.Operators;
import util.rules.nonpayload.NonPayloadOptions;

/**
 * Created by joao on 29/10/15.
 */
public class NonPayloadMatcher {
    public static boolean match(PacketData packetData, NonPayloadOptions nonPayload){
        boolean match = true;
        match &= matchFragoffset(packetData, nonPayload, match);
        if(match)
            match &= matchTTL(packetData, nonPayload, match);
        if(match)
            match &= matchTos(packetData, nonPayload, match);
        return match;
    }

    private static boolean matchFragoffset(PacketData packetData, NonPayloadOptions nonPayload, boolean match) {
        if(nonPayload.fragoffset != null) {
            if (nonPayload.fragoffset.operation != 0)
                match &= Operators.Compare(nonPayload.fragoffset.operation, nonPayload.fragoffset.fragoffset, packetData.fragoffset);
            else
                match &= nonPayload.fragoffset.fragoffset == packetData.fragoffset;
            return match;
        }
        return match;
    }

    private static boolean matchTTL(PacketData packetData, NonPayloadOptions nonPayload, boolean match) {
        if(nonPayload.ttl != null) {
            if (nonPayload.ttl.operation != 0)
                match &= Operators.Compare(nonPayload.ttl.operation, nonPayload.ttl.min, packetData.TTL);
            else
                match &= nonPayload.ttl.min == packetData.TTL;
            return match;
        }
        return match;
    }

    private static boolean matchTos(PacketData packetData, NonPayloadOptions nonPayload, boolean match) {
        if(nonPayload.tos != null) {
            if (nonPayload.tos.operation != 0)
                match &= Operators.Compare(nonPayload.tos.operation, nonPayload.tos.tos, packetData.tos);
            else
                match &= nonPayload.tos.tos == packetData.tos;
            return match;
        }
        return match;
    }
}
