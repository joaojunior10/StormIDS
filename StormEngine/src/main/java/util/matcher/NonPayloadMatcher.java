package util.matcher;

import util.packetdata.PacketData;
import util.rules.Operators;
import util.rules.nonpayload.NonPayloadOptions;

/**
 * Created by joao on 29/10/15.
 */
public class NonPayloadMatcher {

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

    private static boolean matchWindows(PacketData packetData, NonPayloadOptions nonPayload, boolean match) {
        if(nonPayload.window != null) {
            if (nonPayload.window.operation != 0)
                match &= Operators.Compare(nonPayload.window.operation, nonPayload.window.window, packetData.tos);
            else
                match &= nonPayload.window.window == packetData.window;
            return match;
        }
        return match;
    }

    private static boolean matchSeq(PacketData packetData, NonPayloadOptions nonPayload, boolean match) {
        if(nonPayload.seq != null) {
            match &= nonPayload.seq == packetData.seq;
            return match;
        }
        return match;
    }

    private static boolean matchAck(PacketData packetData, NonPayloadOptions nonPayload, boolean match) {
        if(nonPayload.ack != null) {
            match &= nonPayload.ack == packetData.ack;
            return match;
        }
        return match;
    }
    public static boolean match(PacketData packetData, NonPayloadOptions nonPayload){
        boolean match = true;
        try {
            match &= matchFragoffset(packetData, nonPayload, match);
            if (!match) return match;
            match &= matchTTL(packetData, nonPayload, match);
            if (!match) return match;
            match &= matchTos(packetData, nonPayload, match);
            if (!match) return match;
            match &= matchWindows(packetData, nonPayload, match);
            if (!match) return match;
            match &= matchSeq(packetData, nonPayload, match);
            if(!match) return match;
            match &= matchAck(packetData, nonPayload, match);

        }catch (Exception e){

        }
        return match;
    }
}
