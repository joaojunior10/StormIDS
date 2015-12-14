package monitor.plugins;

import java.util.TreeMap;
import java.util.Map;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import monitor.plugins.prototype.SystemResourcePlugin;
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
	public JsonObject getSystemInformation() {
		
		try{
			mem = sigar.getMem();
			Map <String, Object> map = new TreeMap<String, Object>();
			map.put("RAM",mem.toMap().toString());
			JsonParser parser = new JsonParser();
			objToReturn = parser.parse(map.toString().replace("=", ":")).getAsJsonObject();
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
