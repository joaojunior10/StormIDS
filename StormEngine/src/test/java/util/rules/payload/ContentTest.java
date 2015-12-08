package util.rules.payload;

import org.junit.Assert;
import org.junit.Test;
import util.rules.Option;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Created by joao on 9/5/15.
 */
public class ContentTest {

    @Test
    public void parseContentWithNoFlag() throws Exception {
        String content = "content:\"|5c 00|P|00|I|00|P|00|E|00 5c|\";";
        Option option = new Option();
        option.parse(content);
        PayloadOptions payloadOptions = new PayloadOptions();

        byte[] bytes = Base64.getDecoder().decode("XABQAEkAUABFAFw=");
        payloadOptions.parse(option);
        Assert.assertEquals(new String(bytes, StandardCharsets.UTF_8),
                payloadOptions.contents.get(0).content);
    }

    @Test
    public void parseContentWithNoCaseFlag() throws Exception {
        String content = "content:\"|5c 00|P|00|I|00|P|00|E|00 5c|\";";
        Option option = new Option();
        option.parse(content);
        PayloadOptions payloadOptions = new PayloadOptions();
        byte[] bytes = Base64.getDecoder().decode("XABwAGkAcABlAFw=");
        payloadOptions.parse(option);
        payloadOptions.contents.get(0).nocase = true;
        payloadOptions.contents.get(0).toNocase();
        Assert.assertEquals(new String(bytes, StandardCharsets.UTF_8),
                payloadOptions.contents.get(0).content);
    }
}