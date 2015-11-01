package monitor.plugins.packetcapture;

import java.util.ArrayList;
import java.util.List;

import monitor.plugins.packetcapture.packetdata.PacketData;

public class PacketQueue {
	public List<PacketData> packets;
	private static PacketQueue instance = null;

	protected PacketQueue() {
		packets = new ArrayList<PacketData>();
	}

	public static PacketQueue getInstance() {
		if(instance == null) {
			instance = new PacketQueue();
		}
		return instance;
	}

	public  String getPackets() {
		synchronized(this.packets){
			String json = toJSON(this.packets);
			this.packets = new ArrayList<PacketData>();
			return json;
		}
	}

	public String toJSON(List<PacketData> packets) {
		StringBuilder result = new StringBuilder();
		result.append("{\"PacketData\":[\n");
		for(PacketData p : packets){
			result.append(p.toString());
			result.append(",\n\n");
		}
		//Remove last comma
		if(!packets.isEmpty())
			result.deleteCharAt(result.length() -3);
		result.append("\n]}\n");
		return result.toString();
	}

	public void addPacket(PacketData p) {
		synchronized(this.packets){
			this.packets.add(p);
		}
	}
}
