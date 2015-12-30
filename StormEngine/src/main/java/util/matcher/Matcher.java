package util.matcher;

import util.packetdata.PacketData;
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
        for(PacketData packet : packets){
            matchRules(packet, hostname);
        }
    }

    private void matchRules(PacketData packet, String hostname) {
        for(SnortSignature rule : snortSignatures){
            boolean match = true;
            match &= HeaderMatcher.match(packet, rule.header);
            if(!match) continue;
            //match &= NonPayloadMatcher.match(packet, rule.nonPayloadOptions);
            match &= PayloadMatcher.match(packet.data, rule.payloadOptions);
            if(match){
                Match matchAlert = new Match(packet,rule,hostname);
                matches.add(matchAlert);
            }
        }
    }


}
