package monitor.plugins;

import monitor.plugins.prototype.SystemResourcePlugin;
import monitor.util.json.JSONObject;

import org.hyperic.sigar.NetRoute;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;


public class NetRouteListPlugin extends SystemResourcePlugin {

	NetRoute[] routes = null;
	Sigar sigar = new Sigar();
	
	public NetRouteListPlugin(Integer period){
		super(period);
	}
	@Override
	public JSONObject getSystemInformation() {
		
		try{
			routes = sigar.getNetRouteList();
			//Map <String, Object> map = new TreeMap<String, Object>();
			objToReturn = new JSONObject();
			int i=0;
			for(NetRoute route: routes){
				
				//map.putAll(route.toMap());
				objToReturn.put("Route"+i, route.toString().replace("=", ":"));
				i++;
			}
			
			//System.out.println("JSON:"+ objToReturn.toString() + "\r\n");
		} catch (SigarException e) {
			e.printStackTrace();
		}

		return objToReturn;
		}
	

	@Override
	public String topicName() {
		return new String("NetRouteList");
	}
	

}
