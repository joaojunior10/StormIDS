package monitor.plugins;

import java.util.Map;
import java.util.TreeMap;

import monitor.plugins.prototype.SystemResourcePlugin;
import monitor.util.json.JSONObject;
import monitor.util.json.JSONTokener;

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
	public JSONObject getSystemInformation() {
		
		try{
			netIntStats = sigar.getNetInterfaceList();
			
			Map <String, Object> map = new TreeMap<String, Object>();
			for(String iface : netIntStats) map.put(iface, sigar.getNetInterfaceStat(iface).toString().replace('{', '"').replace('}','"'));
			
			objToReturn = new JSONObject(new JSONTokener(map.toString().replace("=", ":")));
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
