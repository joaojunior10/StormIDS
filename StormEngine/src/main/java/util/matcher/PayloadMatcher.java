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
        Matcher matcher = payload.pattern.matcher(stringData);
        match &= matcher.find();
        return match;
    }
}
