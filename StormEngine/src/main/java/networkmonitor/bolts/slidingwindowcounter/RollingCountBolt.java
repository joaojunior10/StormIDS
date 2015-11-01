package networkmonitor.bolts.slidingwindowcounter;

import networkmonitor.bolts.slidingwindowcounter.tools.AbstractSlidingWindowCounterBolt;

import org.apache.log4j.Logger;

import util.json.JSONObject;


public class RollingCountBolt extends AbstractSlidingWindowCounterBolt {

    private static final long serialVersionUID = 5537727428628598519L;
    private static final Logger LOG = Logger.getLogger(RollingCountBolt.class);

    
    public RollingCountBolt(String topic) {
    	super(topic);
    	
    }

    public RollingCountBolt(String topic, int windowLengthInSeconds, int emitFrequencyInSeconds) {
        super(topic,windowLengthInSeconds, emitFrequencyInSeconds);
    }

	@Override
	protected void countObj(JSONObject jsonObj) {
		{
			String hostname = jsonObj.getString("hostname");
			Double count = jsonObj.getDouble(topic);
	        counter.increaseCount(hostname,count);
	   
	    }
	    
		
	}
	

}
