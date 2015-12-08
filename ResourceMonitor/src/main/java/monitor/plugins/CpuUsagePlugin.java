package monitor.plugins;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import monitor.plugins.prototype.SystemResourcePlugin;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;


public class CpuUsagePlugin extends SystemResourcePlugin {
	Sigar sigar = new Sigar();

	public CpuUsagePlugin(Integer period){
		super(period);
	}

	public JsonObject getSystemInformation() {
		CpuPerc info = null;

		try {
			info = sigar.getCpuPerc();
			String value = ((Double)info.getCombined()).toString();
			Map <String,String> map = new HashMap<String, String>();
			map.put(topicName(), value);
			//System.out.println("Map:"+ map.toString() + "\r\n");
			JsonParser parser = new JsonParser();
			objToReturn = parser.parse(map.toString().replace("=", ":")).getAsJsonObject();			//System.out.println("JSON:"+ objToReturn.toString() + "\r\n");
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
