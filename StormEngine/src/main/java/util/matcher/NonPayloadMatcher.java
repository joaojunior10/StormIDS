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
                match &= Operators.Compare(nonPayload.ttl.operation, nonPayload.ttl.max, packetData.TTL);
            else
                match &= nonPayload.ttl.max == packetData.TTL;
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

    private static boolean matchDsize(PacketData packetData, NonPayloadOptions nonPayload, boolean match) {
        if(nonPayload.dsize != null) {
            if (nonPayload.dsize.operation != 0)
                match &= Operators.Compare(nonPayload.dsize.operation, nonPayload.dsize.max, packetData.dsize);
            else
                match &= nonPayload.dsize.max == packetData.dsize;
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

    private static boolean matchId(PacketData packetData, NonPayloadOptions nonPayload, boolean match) {
        if(nonPayload.id != null) {
            match &= nonPayload.id == packetData.id;
            return match;
        }
        return match;
    }
    private static boolean matchItype(PacketData packetData, NonPayloadOptions nonPayload, boolean match) {
        if(nonPayload.itype != null) {
            if (nonPayload.itype.operation != 0)
                match &= Operators.Compare(nonPayload.itype.operation, nonPayload.itype.max, packetData.itype);
            else
                match &= nonPayload.itype.max == packetData.itype;
            return match;
        }
        return match;
    }

    private static boolean matchIcode(PacketData packetData, NonPayloadOptions nonPayload, boolean match) {
        if(nonPayload.icode != null) {
            if (nonPayload.icode.operation != 0)
                match &= Operators.Compare(nonPayload.icode.operation, nonPayload.icode.max, packetData.icode);
            else
                match &= nonPayload.icode.max == packetData.icode;
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
            if(!match) return match;
            match &= matchId(packetData, nonPayload, match);
            if(!match) return match;
            match &= matchDsize(packetData, nonPayload, match);
            if(!match) return match;
            match &= matchItype(packetData, nonPayload, match);
            if(!match) return match;
            match &= matchIcode(packetData, nonPayload, match);
        }catch (Exception e){

        }
        return match;
    }
}
