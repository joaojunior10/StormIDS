package monitor.plugins;

import java.util.Map;
import java.util.TreeMap;

import monitor.plugins.prototype.SystemResourcePlugin;
import monitor.util.json.JSONObject;
import monitor.util.json.JSONTokener;

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
	public JSONObject getSystemInformation() {
		CpuInfo[] cpus = null;
		try {
			cpus = sigar.getCpuInfoList();
			Map<String, Object> map = new TreeMap<String, Object>();
			int i = 0;
			for (CpuInfo cpu : cpus) {
				map.put("CPU" + i, '"' + cpu.toMap().toString() + '"');
				i++;
			}
			objToReturn = new JSONObject(new JSONTokener(map.toString()
					.replace("=", ":")));
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
