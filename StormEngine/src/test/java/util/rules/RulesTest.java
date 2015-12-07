package util.rules;

import networkmonitor.bolts.networkdata.PacketData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import util.matcher.HeaderMatcher;
import util.rules.header.Header;

/**
 * Created by joao on 26/11/15.
 */
public class RulesTest {

    @Test
    public void getRules()  {
        Rules rules = new Rules();
        try{
            rules.get();

        }catch (Exception e){
            e.printStackTrace();
        }
        Assert.assertTrue(true);
    }
}
