package networkmonitor.bolts.networkdata;

import org.junit.Assert;
import util.matcher.Matcher;
import util.rules.Rules;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by joao on 9/6/15.
 */
public class NetworkDataBoltTest {
    public void testTreatData() throws Exception {
        List<PacketData> packets = new ArrayList<PacketData>();
        PacketData packet = new PacketData();
        packet.data = "bHCf1cIeuOhWPQk4CABFAAG0OtFAAEAGZEgKAAEFa7QjcvsOAFBiRBoD4PbsZ4AYEAAWKwAAAQEICkb29FiEAav5R0VUIC9zdHlsZS9pbWFnZXMvZmF2aWNvbi5wbmcgSFRUUC8xLjENCkhvc3Q6IHd3dy5sZWdhcC5jb20uYnINCkNvbm5lY3Rpb246IGtlZXAtYWxpdmUNClVzZXItQWdlbnQ6IE1vemlsbGEvNS4wIChNYWNpbnRvc2g7IEludGVsIE1hYyBPUyBYIDEwXzEwXzUpIEFwcGxlV2ViS2l0LzUzNy4zNiAoS0hUTUwsIGxpa2UgR2Vja28pIENocm9tZS80NS4wLjI0NTQuODUgU2FmYXJpLzUzNy4zNg0KQWNjZXB0OiAqLyoNCkROVDogMQ0KUmVmZXJlcjogaHR0cDovL3d3dy5sZWdhcC5jb20uYnIvDQpBY2NlcHQtRW5jb2Rpbmc6IGd6aXAsIGRlZmxhdGUsIHNkY2gNCkFjY2VwdC1MYW5ndWFnZTogZW4tVVMsZW47cT0wLjgscHQtQlI7cT0wLjYscHQ7cT0wLjQsZW4tQVU7cT0wLjINCg0K";
        packet.sourceIP = InetAddress.getLocalHost().getHostAddress();
        packet.destinationIP = "107.180.35.114";
        packet.sourcePort = "64270";
        packet.destinationPort = "80";
        packets.add(packet);
        Matcher matcher = new Matcher(new Rules().get());
        matcher.match(packets,"PC-Joao");

        Assert.assertTrue(matcher.matches.size() > 0);

    }
}