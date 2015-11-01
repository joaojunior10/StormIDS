package util.matcher;

import networkmonitor.bolts.networkdata.PacketData;
import util.rules.SnortSignature;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by joao on 9/6/15.
 */
public class Matcher implements Serializable {
    public List<Match> matches;
    private List<SnortSignature> snortSignatures;

    public Matcher(List<SnortSignature> snortSignatures){
        this.snortSignatures = snortSignatures;
        this.matches = new ArrayList<Match>();
    }
    public void match(List<PacketData> packets, String hostname){
        //TODO create threads

        for(PacketData packet : packets){
            matchRules(packet, hostname);
        }
    }

    private void matchRules(PacketData packet, String hostname) {

        for(SnortSignature rule : snortSignatures){
            boolean match = true;
            match &= matchHeader(packet, rule);
            //match &= NonPayloadMatcher.match(packet, rule.nonPayloadOptions);
            match &= PayloadMatcher.match(packet.data, rule.payloadOptions);
            if(match){
                Match matchAlert = new Match(packet,rule,hostname);
                matches.add(matchAlert);
            }
        }
    }

    private boolean matchHeader(PacketData packet, SnortSignature rule) {
        boolean match = true;
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
