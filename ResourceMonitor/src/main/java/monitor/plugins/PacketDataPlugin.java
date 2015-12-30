package monitor.plugins;

import java.util.List;

import monitor.plugins.packetcapture.PacketCapture;
import monitor.plugins.packetcapture.PacketQueue;
import util.packetdata.PacketData;
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
