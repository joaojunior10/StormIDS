package networkmonitor.bolts.networkdata;

import java.util.HashMap;
import java.util.Map;

public class PacketData {

	public PacketData(PacketData packet) {
		super();
		this.data = packet.data;
		this.sourceIP = packet.sourceIP;
		this.destinationIP = packet.destinationIP;
		this.sourcePort = packet.sourcePort;
		this.destinationPort = packet.destinationPort;
		this.fragoffset = packet.fragoffset;
		this.TTL = packet.TTL;
		this.tos = packet.tos;
		this.id = packet.id;
		this.ipopts = packet.ipopts;
		this.fragbits = packet.fragbits;
		this.dsize = packet.dsize;
		this.flags = packet.flags;
		this.flow = packet.flow;
		this.seq = packet.seq;
		this.ack = packet.ack;
		this.window = packet.window;
		this.icmp = packet.icmp;
		this.icode = packet.icode;
		this.icmp_id = packet.icmp_id;
		this.icmp_seq = packet.icmp_seq;
		this.ip_proto_int = packet.ip_proto_int;
		this.ip_proto_string = packet.ip_proto_string;
		this.sameip = packet.sameip;
	}
	public String data;
	
	//Address
	public String sourceIP;
	public String destinationIP;
	public String sourcePort;
	public String destinationPort;
	
	//IP fields
	public int fragoffset;
	public int TTL;
	public int tos;
	public int id;
	public Map<String,Integer> ipopts;
	public Fragbits fragbits;
	public int dsize; //payload size
	
	//TCP fields
	public Map<String,Integer> flags;
	public Map<Integer,Integer> flow;
	public int seq;
	public int ack;
	public int window;
	
	//ICMP
	public int icmp;
	public int icode;
	public int icmp_id;
	public int icmp_seq;
	
	//other options
	public int ip_proto_int;
	public String ip_proto_string;
	public int sameip;
	
	public PacketData(){
		fragbits = new Fragbits();
		ipopts = new HashMap<String, Integer>();
		flags = new HashMap<String, Integer>();
		flow = new HashMap<Integer, Integer>();
	}
	
	public String toString(){
		StringBuilder result = new StringBuilder();
		result.append("{");
		result.append("\"sourceIP\": " + "\""+ sourceIP + "\"");
		result.append(",\n");
		result.append("\"destinationIP\": " + "\""+ destinationIP + "\"");
		result.append(",\n");
		result.append("\"sourcePort\": " + "\""+ sourcePort + "\"");
		result.append(",\n");
		result.append("\"destinationPort\": " + "\""+ destinationPort + "\"");
		result.append(",\n");
		result.append("\"flagoffset\": " + "\""+ fragoffset + "\"");
		result.append(",\n");
		result.append("\"TTL\": " + "\""+ TTL + "\"");
		result.append(",\n");
		result.append("\"tos\": " + "\""+ tos + "\"");
		result.append(",\n");
		result.append("\"id\": " + "\""+ id + "\"");
		result.append(",\n");
		result.append("\"dsize\": " + "\""+ dsize + "\"");
		result.append(",\n");
		result.append("\"data\": " + "\""+ data + "\"");
		result.append(",\n");
		result.append("}\n");

		return result.toString();
	}
}
