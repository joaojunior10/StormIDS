package util.rules.nonpayload;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import util.rules.Operators;
import util.rules.SnortSignature;
import util.rules.general.GeneralOptions;
import util.rules.header.Header;

/**
 * Created by joao on 7/11/15.
 */
public class NonPayloadOptionsTest {
    private SnortSignature _snortSignature;
    @Before
    public void setup(){
        _snortSignature = new SnortSignature();
    }
    @Test
    public void parseOptions() throws Exception {
        String rules = "alert tcp $HOME_NET any -> $EXTERNAL_NET 20 " +
                "(msg:\"MALWARE-TOOLS Browser Password Decryptor - Password List sent via FTP\"; " +
                "flow:to_client,established; content:\"Browser Password Recovery Report|0D 0A|\"; " +
                "nocase; content:\"Password List|20 0D 0A|\"; distance:0; nocase;fragbits:M;" +
                " fragoffset:!400; ttl:>=5;tos:4; id:31337; ipopts:lsrr; fragbits:MD+; flags:SF,CE;)";
        _snortSignature.parse(rules);

        NonPayloadOptions nonPayloadOptions = _snortSignature.nonPayloadOptions;
        Assert.assertEquals(400, nonPayloadOptions.fragoffset.fragoffset);
        Assert.assertEquals(5, nonPayloadOptions.ttl.max);
        Assert.assertEquals(Operators.GREATEROREQUALTHAN, nonPayloadOptions.ttl.operation);
        Assert.assertEquals(31337, nonPayloadOptions.id.intValue());
        Assert.assertEquals(4, nonPayloadOptions.tos.tos);
        Assert.assertEquals("lsrr",nonPayloadOptions.ipopts);
        Assert.assertEquals("SF", nonPayloadOptions.flags.flags.get(0));
        Assert.assertEquals("CE", nonPayloadOptions.flags.flags.get(1));
    }
}
