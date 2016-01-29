package util.matcher;

import org.junit.Assert;
import org.junit.Test;
import util.rules.payload.PayloadOptions;
import util.rules.payload.RegexGenerator;
import util.rules.payload.options.Content;

/**
 * Created by joao on 9/6/15.
 */
public class PayloadMatcherTest {

    @Test
    public void matchPayloadWithDepthAndOffset() throws Exception {
        PayloadOptions payloadOptions = new PayloadOptions();
        Content content = new Content();
        content.content = " /../";
        content.depth = 40;
        content.offset = 5;
        payloadOptions.contents.add(content);
        payloadOptions.pattern = RegexGenerator.generate(payloadOptions.contents);

        String data = "kdddddnfcsdcdcdscsdcdABCcxzczxczxczcxczx /../";
        boolean match = PayloadMatcher.match(data.getBytes(), payloadOptions);
        Assert.assertTrue(match);

    }
    @Test
    public void matchPayloadWithNocase() throws Exception {
        PayloadOptions payloadOptions = new PayloadOptions();
        Content content = new Content();
        content.content = "def";
        content.depth = 40;
        content.offset = 5;
        content.nocase = true;
        payloadOptions.contents.add(content);
        payloadOptions.pattern = RegexGenerator.generate(payloadOptions.contents);

        String data = "dddddnfcsdcdcdscsdcdABCcxzczxczxczcxczxDEF";
        boolean match = PayloadMatcher.match(data.getBytes(), payloadOptions);
        Assert.assertTrue(match);

    }
    @Test
    public void matchPayloadWithNocaseTwoContents() throws Exception {
        PayloadOptions payloadOptions = new PayloadOptions();
        Content content = new Content();
        content.content = "def";
        content.depth = 40;
        content.offset = 5;
        content.nocase = true;
        payloadOptions.contents.add(content);

        content = new Content();
        content.content = "ABC";
        content.distance = 1;
        content.nocase = true;
        payloadOptions.contents.add(content);

        payloadOptions.pattern = RegexGenerator.generate(payloadOptions.contents);

        String data = "kdddddnfcsdcdcdscsdcdABCcxzczxczxczcxczxDEF ABC";
        boolean match = PayloadMatcher.match(data.getBytes(), payloadOptions);
        Assert.assertTrue(match);

    }
}