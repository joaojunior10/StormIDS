package util.rules.header;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by joao on 7/11/15.
 */
public class HeaderTest {
    private Header _header;
    @Before
    public void setup(){
        _header = new Header();
    }
    @Test
    public void parseHeader() throws Exception {
        String headerWithIpMask = "alert tcp $HOME_NET any -> 192.168.1.0/24 111";
        _header.parse(headerWithIpMask);

        Assert.assertEquals("alert", _header.action);
        Assert.assertEquals("tcp",_header.protocol);
        Assert.assertTrue(_header.ipsSrc.containsKey("10.0.1.5"));
        Assert.assertTrue(_header.portsSrc.containsKey("any"));
        Assert.assertEquals(Header.DIRECTIONMAP.get("->"), new Integer(_header.direction));

        Assert.assertTrue(_header.ipsDst.containsKey("192.168.1.0/24"));
        Assert.assertTrue(_header.portsDst.containsKey("111"));
    }
}
