package monitor.plugins;

import java.util.TreeMap;
import java.util.Map;

import monitor.plugins.prototype.SystemResourcePlugin;
import monitor.util.json.JSONObject;
import monitor.util.json.JSONTokener;

import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;



public class MemInfoPlugin extends SystemResourcePlugin {

	Mem mem = null;
	Sigar sigar = new Sigar();
	
	public MemInfoPlugin(Integer period){
		super(period);
	}
	@Override
	public JSONObject getSystemInformation() {
		
		try{
			mem = sigar.getMem();
			Map <String, Object> map = new TreeMap<String, Object>();
			map.put("RAM",mem.toMap().toString());
			objToReturn = new JSONObject(new JSONTokener(map.toString().replace("=", ":")));
		} catch (SigarException e) {
			e.printStackTrace();
		}

		return objToReturn;
		}


	@Override
	public String topicName() {
		return new String("MemInfo");
	}

}
