package monitor.plugins;

import java.util.HashMap;
import java.util.Map;

import monitor.plugins.prototype.SystemResourcePlugin;
import monitor.util.json.JSONObject;
import monitor.util.json.JSONTokener;

import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;


public class MemUsagePlugin extends SystemResourcePlugin {

	Sigar sigar = new Sigar();

	public MemUsagePlugin(Integer period){
		super(period);
	}

	public JSONObject getSystemInformation() {
		Mem info = null;

		try {
			info = sigar.getMem();
			String value = ((Double)(info.getUsedPercent()/100)).toString();
			Map <String,String> map = new HashMap<String, String>();
			map.put(topicName(), value);
			//System.out.println("Map:"+ map.toString() + "\r\n");
			objToReturn = new JSONObject(new JSONTokener(map.toString().replace("=", ":")));
			//System.out.println("JSON:"+ objToReturn.toString() + "\r\n");
		} catch (SigarException e) {
			e.printStackTrace();
		}

		return objToReturn;
	}

	@Override
	public String topicName() {
		return new String("MemUsage");
	}


}
