package monitor.plugins;

import java.util.HashMap;
import java.util.Map;

import monitor.plugins.prototype.SystemResourcePlugin;
import monitor.util.json.JSONObject;
import monitor.util.json.JSONTokener;

import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;


public class FileSystemUsagePlugin extends SystemResourcePlugin {

	Sigar sigar = new Sigar();

	public FileSystemUsagePlugin(Integer period){
		super(period);
	}

	public JSONObject getSystemInformation() {
		FileSystemUsage info = null;

		try {
			info = sigar.getFileSystemUsage("/");
			
			String value = ((Double)info.getUsePercent()).toString();
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
		return new String("FileSystemUsage");
	}

//	public static void main(String[] args) {
//
//		//Add the plugins
//		FileSystemUsagePlugin plugin1 = new FileSystemUsagePlugin(1000);
//		plugin1.getSystemInformation();
//	}

}
