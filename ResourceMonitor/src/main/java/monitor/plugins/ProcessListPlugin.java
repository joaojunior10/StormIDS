package monitor.plugins;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import monitor.plugins.prototype.SystemResourcePlugin;

import org.hyperic.sigar.NetRoute;
import org.hyperic.sigar.ProcCredName;
import org.hyperic.sigar.ProcMem;
import org.hyperic.sigar.ProcState;
import org.hyperic.sigar.ProcTime;
import org.hyperic.sigar.ProcUtil;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.cmd.Shell;

public class ProcessListPlugin extends SystemResourcePlugin {

	NetRoute[] routes = null;
	Sigar sigar = new Sigar();
	Shell a = new Shell(); 
	Map<String, Object> toSend = new TreeMap<String, Object>();

	public ProcessListPlugin(Integer period){
		super(period);
	}
	@Override
	public JsonObject getSystemInformation() {

		try{
			routes = sigar.getNetRouteList();
			//Map <String, Object> map = new TreeMap<String, Object>();
			int i=0;

			long[] pidList = sigar.getProcList();
			for(i=0; i<pidList.length; i++)
			{
				output(pidList[i]);
			}
			Gson gson = new Gson();
			JsonParser parser = new JsonParser();
			objToReturn = parser.parse(gson.toJson(toSend)).getAsJsonObject();

			//System.out.println("JSON:"+ objToReturn.toString() + "\r\n");
		} catch (SigarException e) {
			e.printStackTrace();
		}

		return objToReturn;
	}


	@Override
	public String topicName() {
		return new String("ProcessList");
	}

	public static void main (String[] args){
		ProcessListPlugin plugin = new ProcessListPlugin(50);
		plugin.getSystemInformation();

	}

	public static String join(List info) {
		//        StringBuffer buf = new StringBuffer();
		//        Iterator i = info.iterator();
		//        boolean hasNext = i.hasNext();
		//        while (hasNext) {
		//            buf.append((String)i.next());
		//            hasNext = i.hasNext();
		//            if (hasNext)
		//                buf.append("\t");
		//        }

		return info.toString();
	}

	public static List getInfo(Sigar sigar, long pid)
			throws SigarException {

		ProcState state = sigar.getProcState(pid);
		ProcTime time = null;
		String unknown = "???";

		List info = new ArrayList();
		//info.add(String.valueOf(pid));

		try {
			ProcCredName cred = sigar.getProcCredName(pid);
			info.add(cred.getUser());
		} catch (SigarException e) {
			info.add(unknown);
		}

		//        try {
		//            time = sigar.getProcTime(pid);
		//            info.add(getStartTime(time.getStartTime()));
		//        } catch (SigarException e) {
		//            info.add(unknown);
		//        }

		try {
			ProcMem mem = sigar.getProcMem(pid);
			//            info.add(Sigar.formatSize(mem.getSize()));
			info.add(Sigar.formatSize(mem.getRss()));
			//            info.add(Sigar.formatSize(mem.getShare()));
		} catch (SigarException e) {
			info.add(unknown);
		}

		//        info.add(String.valueOf(state.getState()));

		//        if (time != null) {
		//            info.add(getCpuTime(time));
		//        }
		//        else {
		//            info.add(unknown);
		//        }

		String name = ProcUtil.getDescription(sigar, pid);
		info.add(name);

		return info;
	}

	public void output(long pid) throws SigarException {
		String ps = join(getInfo(sigar, pid));
		toSend.put(new Long(pid).toString(), ps);
		//System.out.println(ps);

	}

	public static String getCpuTime(long total) {
		long t = total / 1000;
		return t/60 + ":" + t%60;
	}

	public static String getCpuTime(ProcTime time) {
		return getCpuTime(time.getTotal());
	}

	private static String getStartTime(long time) {
		if (time == 0) {
			return "00:00";
		}
		long timeNow = System.currentTimeMillis();
		String fmt = "MMMd";

		if ((timeNow - time) < ((60*60*24) * 1000)) {
			fmt = "HH:mm";
		}

		return new SimpleDateFormat(fmt).format(new Date(time));
	}
}
