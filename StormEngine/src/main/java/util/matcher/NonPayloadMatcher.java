package util.matcher;

import networkmonitor.bolts.networkdata.PacketData;
import util.rules.Operators;
import util.rules.nonpayload.NonPayloadOptions;

/**
 * Created by joao on 29/10/15.
 */
public class NonPayloadMatcher {
    public static boolean match(PacketData packetData, NonPayloadOptions nonPayload){
        boolean match = true;
        match = matchFragoffset(packetData, nonPayload, match);
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
}
