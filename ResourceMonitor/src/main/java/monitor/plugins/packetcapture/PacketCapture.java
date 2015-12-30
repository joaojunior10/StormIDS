package monitor.plugins.packetcapture;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import monitor.Config;
import util.packetdata.PacketData;

import org.pcap4j.core.*;
import org.pcap4j.core.PcapNetworkInterface.PromiscuousMode;
import org.pcap4j.packet.IpV4Packet;
import org.pcap4j.packet.IpV4Packet.IpV4Header;
import org.pcap4j.packet.IpV6Packet;
import org.pcap4j.packet.IpV6Packet.IpV6Header;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.TcpPacket;
import org.pcap4j.packet.TcpPacket.TcpHeader;
import org.pcap4j.packet.UdpPacket;
import org.pcap4j.packet.UdpPacket.UdpHeader;
import org.pcap4j.util.NifSelector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PacketCapture extends Thread {
	private static PacketCapture instance = null;
	private String bfp;
	private int count = 0;
	private static final Logger LOG = LoggerFactory.getLogger(PacketCapture.class);

	public static PacketCapture getInstance() {
		if(instance == null) {
			instance = new PacketCapture();
		}
		return instance;
	}

	public PacketCapture(){
		bfp = Config.getInstance().bfp;
	}

	public void run(){
		PcapNetworkInterface nif = null;
		try {
			nif = new NifSelector().selectNetworkInterface();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		if (nif == null) {
			System.out.println("No network interface was found");
			return;
		}

		System.out.println(nif.getName() + "(" + nif.getDescription() + ")");
		final Map<Short, List<IpV4Packet>> ipV4Packets
		= new HashMap<Short, List<IpV4Packet>>();
		final Map<Short, Packet> originalPackets = new HashMap<Short, Packet>();

		try {
			final PcapHandle handle;
			handle = nif.openLive(65536, PromiscuousMode.PROMISCUOUS, 10);
			//Set a filter if it is configured
			if(bfp != null) {
				handle.setFilter(
                        bfp,
                        BpfProgram.BpfCompileMode.OPTIMIZE
                );
			}
			//Start Listening
			PacketListener listener
			= new PacketListener() {
				public void gotPacket(Packet packet) {
					PacketData p = getPacketData(packet);
					PacketQueue.getInstance().addPacket(p);
					count++;
					if(count%1000 == 0){
						LOG.trace(Integer.toString(count));
					}
				}
			};		
			ExecutorService pool = Executors.newCachedThreadPool();
			handle.loop(-1, listener, pool); // This is better than handle.loop(5, listener);
			pool.shutdown();
			handle.close();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (PcapNativeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotOpenException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private PacketData getPacketData(Packet packet) {
		PacketData packetData = new PacketData();
		//packetData.connectionId = id;
		if(packet.get(IpV4Packet.class) != null){
			IpV4Header ipPacket = packet.get(IpV4Packet.class).getHeader();
			packetData.sourceIP = ipPacket.getSrcAddr().toString().replace('/', '\0').trim();
			packetData.destinationIP = ipPacket.getDstAddr().toString().replace('/', '\0').trim();
			packetData.fragoffset = ipPacket.getFragmentOffset();
			packetData.TTL = ipPacket.getTtlAsInt();
			packetData.tos =  ipPacket.getTos().value();
			packetData.id = ipPacket.getIdentificationAsInt();
			//TODO packetData.ipopts = ipPacket.getOptions();
			packetData.fragbits.D = ipPacket.getDontFragmentFlag();
			packetData.fragbits.M = ipPacket.getMoreFragmentFlag();
			packetData.fragbits.R = ipPacket.getReservedFlag();
			packetData.dsize = ipPacket.getTotalLengthAsInt();
		}
		else if(packet.get(IpV6Packet.class) != null){
			IpV6Header ipPacket = packet.get(IpV6Packet.class).getHeader();
			packetData.sourceIP = ipPacket.getSrcAddr().toString().replace('/', '\0').trim();
			packetData.destinationIP = ipPacket.getDstAddr().toString().replace('/', '\0').trim();
			packetData.dsize = ipPacket.getPayloadLength();
		}

		//Transport layer information
		if(packet.get(TcpPacket.class) != null){
			TcpHeader tcpPacket = packet.get(TcpPacket.class).getHeader();
			packetData.sourcePort = tcpPacket.getSrcPort().toString().split(" ")[0].trim();
			packetData.destinationPort = tcpPacket.getDstPort().toString().split(" ")[0].trim();
			// TODO packetData.flags
			// TODO packetData.flow
			packetData.seq = tcpPacket.getSequenceNumber();
			packetData.ack = tcpPacket.getAcknowledgmentNumber();
			packetData.window = tcpPacket.getWindowAsInt();
			packetData.protocol = "TCP";
		}
		else if(packet.get(UdpPacket.class) != null){
			UdpHeader udpPacket = packet.get(UdpPacket.class).getHeader();
			packetData.sourcePort = udpPacket.getSrcPort().toString().split(" ")[0].trim();
			packetData.destinationPort = udpPacket.getDstPort().toString().split(" ")[0].trim();
			packetData.protocol = "UDP";
		}
		packetData.data =  packet.getRawData();
		return packetData;
	}
}
