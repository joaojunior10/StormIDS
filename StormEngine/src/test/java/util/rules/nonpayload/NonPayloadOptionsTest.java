package util.rules.nonpayload;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
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
                "nocase; content:\"Password List|20 0D 0A|\"; distance:0; nocase; metadata:impact_flag red, " +
                "policy balanced-ips drop, policy security-ips drop, service ftp-data; " +
                "reference:url,www.virustotal.com/en/file/7cf757e0943b0a6598795156c156cb90feb7d87d4a22c01044499c4e1619ac57/analysis/;" +
                " classtype:trojan-activity; sid:29096; rev:1;)";
        _snortSignature.parse(rules);

        GeneralOptions generalOptions = _snortSignature.generalOptions;
        Assert.assertEquals("MALWARE-TOOLS Browser Password Decryptor - Password List sent via FTP", generalOptions.msg);
        Assert.assertEquals("trojan-activity", generalOptions.classtype);
        Assert.assertEquals("1", generalOptions.rev.toString());
        Assert.assertEquals("29096", generalOptions.sid.toString());
        Assert.assertEquals("http://www.virustotal.com/en/file/7cf757e0943b0a6598795156c156cb90" +
                "feb7d87d4a22c01044499c4e1619ac57/analysis/", generalOptions.references.get(0));
        Assert.assertEquals("red", generalOptions.metadata.get(0).get("impact_flag"));
        Assert.assertEquals("balanced-ips drop", generalOptions.metadata.get(1).get("policy"));
        Assert.assertEquals("security-ips drop", generalOptions.metadata.get(2).get("policy"));
        Assert.assertEquals("ftp-data", generalOptions.metadata.get(3).get("service"));
    }
}
