package util.matcher;

import util.rules.payload.PayloadOptions;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.regex.Matcher;

/**
 * Created by joao on 9/5/15.
 */
public class PayloadMatcher {
    public static boolean match(byte[] packetData, PayloadOptions payload){
        if (payload.contents.size() > 0) {
            boolean match = true;
            String stringData = new String(packetData, StandardCharsets.ISO_8859_1);
            Matcher matcher = payload.pattern.matcher(stringData);
            match &= matcher.find();
            return match;
        }
        else return false;
    }
}
