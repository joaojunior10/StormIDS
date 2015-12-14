package monitor.plugins.packetcapture;

public class Flow {
	public long id = 0;
	public String sourceIP;
	public String destinationIP;
	public String sourcePort;
	public String destinationPort;
	public long bytes = 0;
	public long packets = 0;
	
	public Flow(long id){
		this.id = id;
	}
	public String getSourceIP() {
		return sourceIP;
	}

	public void setSourceIP(String sourceIP) {
		this.sourceIP = sourceIP;
	}

	public String getDestinationIP() {
		return destinationIP;
	}

	public void setDestinationIP(String destinationIP) {
		this.destinationIP = destinationIP;
	}

	public String getSourcePort() {
		return sourcePort;
	}

	public void setSourcePort(String sourcePort) {
		this.sourcePort = sourcePort;
	}

	public String getDestinationPort() {
		return destinationPort;
	}

	public void setDestinationPort(String destinationPort) {
		this.destinationPort = destinationPort;
	}
	
	public long getBytes() {
		return bytes;
	}
	public void setBytes(long bytes) {
		this.bytes += bytes;
	}
	public long getPackets() {
		return packets;
	}
	public void setPackets() {
		this.packets++;
	}
	public String toString(){
		StringBuilder result = new StringBuilder();
		result.append("{");
		result.append("\"sourceIP\": " + "\""+ sourceIP + "\"");
		result.append(",\n");
		result.append("\"sourcePort\": " + "\""+ sourcePort + "\"");
		result.append(",\n");
		result.append("\"destinationIP\": " + "\""+ destinationIP + "\"");
		result.append(",\n");
		result.append("\"destinationPort\": " + "\""+ destinationPort + "\"");
		result.append(",\n");
		result.append("\"bytes\": " + "\""+ bytes + "\"");
		result.append(",\n");
		result.append("\"packets\": " + "\""+ packets + "\"");
		result.append(",\n");
		result.append("}\n");

		return result.toString();
		
	}
}
