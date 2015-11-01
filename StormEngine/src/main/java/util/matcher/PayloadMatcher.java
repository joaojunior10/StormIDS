package util.matcher;

import networkmonitor.bolts.networkdata.PacketData;
import util.patternmatch.KMPMatch;
import util.rules.payload.PayloadOptions;
import util.rules.payload.options.Content;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by joao on 9/5/15.
 */
public class PayloadMatcher {
    public static boolean match(String packetData, PayloadOptions payload){
        boolean match = true;
        byte[] data = Base64.getDecoder().decode(packetData);
        String stringData = new String(data, StandardCharsets.ISO_8859_1);
        StringBuilder regex = new StringBuilder();
        for(Content content : payload.contents){
            if(content.rawbytes){
                data =  Arrays.copyOfRange(data,content.offset,content.depth);
                match &= KMPMatch.indexOf(content.bytecontent,data) != -1;
            }
            else{
                String depth = "";
                String offset = "";
                String distance = "0";
                String nocase = "";
                if(content.nocase){
                    nocase = "(?i)";
                }
                if(content.depth != null) {
                    depth = content.depth.toString();
                }
                if(content.offset != null) {
                    offset = "^";
                    distance = content.offset.toString();
                }
                if(content.distance != null){
                    distance = content.distance.toString();
                }
                if(content.within != null){
                    depth = content.within.toString();
                }
                //TODO create regular expression in the rule creation
                String contentPattern = String.format("%s%s.{%s,%s}%s",nocase,offset, distance,depth,content.content);
                regex.append(contentPattern);
            }
        }

        Pattern pattern;

        pattern = Pattern.compile(regex.toString());
        Matcher matcher = pattern.matcher(stringData);

        match &= matcher.find();
        return match;
    }
}
