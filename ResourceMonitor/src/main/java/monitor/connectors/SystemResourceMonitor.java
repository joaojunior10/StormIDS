package monitor.connectors;


import java.util.ArrayList;
import java.util.List;

import monitor.plugins.prototype.SystemResourcePlugin;

public abstract class SystemResourceMonitor implements Runnable{
	protected final String host;
	protected final int port;
	
	//Default frequency that the monitor will send information to the server
	protected static final int DEFAULT_VERY_SHORT_PROBE_TIME_INTERVAL = 100;
	protected static final int DEFAULT_SHORT_PROBE_TIME_INTERVAL = 1000;
	protected static final int DEFAULT_LONG_PROBE_TIME_INTERVAL = 60*1000;
	protected static final int DEFAULT_CHANNEL_CHECK_TIME_INTERVAL = DEFAULT_LONG_PROBE_TIME_INTERVAL;
	
	protected List<SystemResourcePlugin> listOfResourceMonitors; 

	public SystemResourceMonitor(String host, int port) {
		this.host = host;
		this.port = port;
		listOfResourceMonitors= new ArrayList<SystemResourcePlugin>();
	}

	public void addResourceMonitor(SystemResourcePlugin resourceMonitor){
		listOfResourceMonitors.add(resourceMonitor);
	}


}
