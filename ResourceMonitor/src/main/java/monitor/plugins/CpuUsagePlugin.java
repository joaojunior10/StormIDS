package monitor.plugins;

import java.util.HashMap;
import java.util.Map;

import monitor.plugins.prototype.SystemResourcePlugin;
import monitor.util.json.JSONObject;
import monitor.util.json.JSONTokener;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;


public class CpuUsagePlugin extends SystemResourcePlugin {
	Sigar sigar = new Sigar();

	public CpuUsagePlugin(Integer period){
		super(period);
	}

	public JSONObject getSystemInformation() {
		CpuPerc info = null;

		try {
			info = sigar.getCpuPerc();
			String value = ((Double)info.getCombined()).toString();
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
		return new String("CpuUsage");
	}

}
