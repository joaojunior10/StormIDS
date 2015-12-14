package monitor.plugins;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import monitor.plugins.prototype.SystemResourcePlugin;
import org.hyperic.sigar.NetInfo;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;


public class NetInfoPlugin extends SystemResourcePlugin {
	Sigar sigar = new Sigar();
	
	
	public NetInfoPlugin(Integer period){
		super(period);
	}

	public JsonObject getSystemInformation() {
		NetInfo netInfo = null;
		
		try {
			netInfo = sigar.getNetInfo();
			String b = netInfo.toMap().toString().replace('=', ':');
			JsonParser parser = new JsonParser();
			objToReturn = parser.parse(b).getAsJsonObject();
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
