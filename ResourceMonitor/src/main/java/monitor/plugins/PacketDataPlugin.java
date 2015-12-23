package monitor.plugins;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import monitor.plugins.packetcapture.PacketCapture;
import monitor.plugins.packetcapture.PacketQueue;
import monitor.plugins.packetcapture.packetdata.PacketData;
import monitor.plugins.prototype.SystemResourcePlugin;

public class PacketDataPlugin extends SystemResourcePlugin{
	public PacketDataPlugin(Integer period){
		super(period);
		if(!PacketCapture.getInstance().isAlive())
			PacketCapture.getInstance().start();
	}
	
	@Override
	public Object getSystemInformation() {
		List<PacketData> packets = PacketQueue.getInstance().getPackets();
		if(packets == null) return null;
		return packets;
	}

	@Override
	public String topicName() {
		return new String("NetworkData");
	}

}
