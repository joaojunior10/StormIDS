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
                " classtype:trojan-activity; sid:29096; rev:1;fragbits:M; fragoffset:0; ttl:>=5;tos:4; id:31337; ipopts:lsrr; fragbits:MD+; flags:SF,CE;)";
        _snortSignature.parse(rules);

        NonPayloadOptions nonPayloadOptions = _snortSignature.nonPayloadOptions;

    }
}
