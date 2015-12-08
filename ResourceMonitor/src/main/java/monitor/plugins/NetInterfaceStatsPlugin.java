package monitor.plugins;

import java.util.Map;
import java.util.TreeMap;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import monitor.plugins.prototype.SystemResourcePlugin;

import org.hyperic.sigar.NetInterfaceStat;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;


public class NetInterfaceStatsPlugin extends SystemResourcePlugin {

	String[] netIntStats = null;
	NetInterfaceStat netIntStat = null;
	Sigar sigar = new Sigar();
	
	public NetInterfaceStatsPlugin(Integer period){
		super(period);
	}
	@Override
	public JsonObject getSystemInformation() {
		
		try{
			netIntStats = sigar.getNetInterfaceList();
			
			Map <String, Object> map = new TreeMap<String, Object>();
			for(String iface : netIntStats) map.put(iface, sigar.getNetInterfaceStat(iface).toString().replace('{', '"').replace('}','"'));
			JsonParser parser = new JsonParser();
			objToReturn = parser.parse(map.toString().replace("=", ":")).getAsJsonObject();
			//System.out.println("JSON:"+ objToReturn.toString() + "\r\n");
		} catch (SigarException e) {
			e.printStackTrace();
		}

		return objToReturn;
		}


	@Override
	public String topicName() {
		return new String("NetInterfaceStats");
	}

}
