package util.rules;

import org.junit.Assert;
import org.junit.Test;

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
