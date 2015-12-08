package monitor.plugins;

import java.util.Map;
import java.util.TreeMap;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import monitor.plugins.prototype.SystemResourcePlugin;

import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

public class CpuInfoPlugin extends SystemResourcePlugin {
	
	CpuInfo[] cpus = null;
	Sigar sigar = new Sigar();

	public CpuInfoPlugin(Integer period) {
		super(period);
	}

	@Override
	public JsonObject getSystemInformation() {
		CpuInfo[] cpus = null;
		try {
			cpus = sigar.getCpuInfoList();
			Map<String, Object> map = new TreeMap<String, Object>();
			int i = 0;
			for (CpuInfo cpu : cpus) {
				map.put("CPU" + i, '"' + cpu.toMap().toString() + '"');
				i++;
			}
			JsonParser parser = new JsonParser();
			objToReturn = parser.parse(map.toString().replace("=", ":")).getAsJsonObject();
		} catch (SigarException e) {
			e.printStackTrace();
		}

		return objToReturn;
	}

	@Override
	public String topicName() {
		return new String("CpuInfo");
	}

}
