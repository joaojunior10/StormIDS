package util.matcher;

import util.packetdata.PacketData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import util.rules.header.Header;

/**
 * Created by joao on 11/11/15.
 */
public class HeaderMatcherTest {
    private Header _header;
    private HeaderMatcher _matcher;
    @Before
    public void setup(){
        _header = new Header();
        _matcher = new HeaderMatcher();
    }
    @Test
    public void matchHeader() throws Exception {
        PacketData packet = new PacketData();
        packet.protocol = "TCP";
        packet.sourceIP = "10.0.1.5";
        packet.sourcePort = "433";
        packet.destinationIP = "192.168.1.5";
        packet.destinationPort = "111";

        String headerWithIpMask = "alert tcp $HOME_NET any -> 192.168.1.5 111";
        _header.parse(headerWithIpMask);

        boolean match = _matcher.match(packet,_header);
        Assert.assertTrue(match);
    }
}
