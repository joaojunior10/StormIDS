package monitor.plugins;

import monitor.plugins.prototype.SystemResourcePlugin;
import monitor.util.json.JSONObject;
import monitor.util.json.JSONTokener;

import org.hyperic.sigar.NetInfo;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;


public class NetInfoPlugin extends SystemResourcePlugin {
	Sigar sigar = new Sigar();
	
	
	public NetInfoPlugin(Integer period){
		super(period);
	}

	public JSONObject getSystemInformation() {
		NetInfo netInfo = null;
		
		try {
			netInfo = sigar.getNetInfo();
			String b = netInfo.toMap().toString().replace('=', ':');
			objToReturn = new JSONObject(new JSONTokener(b));
			//System.out.println("JSON:"+ objToReturn.toString() + "\r\n");
		} catch (SigarException e) {
			e.printStackTrace();
		}
		
		return objToReturn;
	}

	@Override
	public String topicName() {
		return new String("NetInfo");
	}


}
