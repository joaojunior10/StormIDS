package monitor.plugins.packetcapture;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import monitor.plugins.packetcapture.packetdata.PacketData;
import monitor.plugins.packetcapture.packetdata.PacketList;

public class PacketQueue {
	public List<PacketData> packets;
	private static PacketQueue instance = null;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	private int total = 0;
	protected PacketQueue() {

		packets = new ArrayList<PacketData>();
	}

	public static PacketQueue getInstance() {
		if(instance == null) {
			instance = new PacketQueue();
		}
		return instance;
	}

	public  List<PacketData> getPackets() {
		synchronized(this.packets){
			if (packets.size() == 0) return null;
			PacketList packetList = new PacketList();
			packetList.packetList = this.packets;
			total += packets.size();
			System.out.println(packets.size() + " packets sent - " +  sdf.format(System.currentTimeMillis()));
			System.out.println("Total -" + total);

			this.packets = new ArrayList<PacketData>();
			return packetList.packetList;
		}
	}

	public void addPacket(PacketData p) {
		synchronized(this.packets){
			this.packets.add(p);
		}
	}
}
