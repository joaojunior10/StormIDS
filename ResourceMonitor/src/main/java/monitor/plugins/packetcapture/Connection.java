package monitor.plugins.packetcapture;

import java.sql.Timestamp;

import monitor.plugins.packetcapture.packetdata.PacketData;

public class Connection {
	public String application;
	public String internetProtocol;
	public String transportProtocol;
	public String clientIP;
	public String serverIP;
	public String clientPort;
	public String serverPort;
	public Timestamp first = null;
	public Timestamp lastSeen;
	public String actualThroughput;
	public long bytes = 0;
	public long packets = 0;
	public long clientToServerTraffic;
	public long serverToClientTraffic;
	public Flow serverToClient;
	public Flow clientToServer;
	
	public Connection() {
		serverToClient = new Flow(FlowId.getInstance().getId());
		clientToServer = new Flow(FlowId.getInstance().getId());
	}	
	public String getApplication() {
		return application;
	}
	public void setApplication(String application) {
		this.application = application;
	}

	public Timestamp getFirst() {
		return first;
	}
	public void setFirst(Timestamp first) {
		this.first = first;
	}
	public Timestamp getLastSeen() {
		return lastSeen;
	}
	public void setLastSeen(Timestamp lastSeen) {
		this.lastSeen = lastSeen;
	}
	public String getActualThroughput() {
		return actualThroughput;
	}
	public void setActualThroughput(String actualThroughput) {
		this.actualThroughput = actualThroughput;
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
	public long getClientToServerTraffic() {
		return clientToServerTraffic;
	}
	public void setClientToServerTraffic(long clientToServerTraffic) {
		this.clientToServerTraffic = clientToServerTraffic;
	}
	public long getServerToClientTraffic() {
		return serverToClientTraffic;
	}
	public void setServerToClientTraffic(long serverToClientTraffic) {
		this.serverToClientTraffic = serverToClientTraffic;
	}
	
	public String getClientIP() {
		return clientIP;
	}

	public void setClientIP(String sourceIP) {
		this.clientIP = sourceIP;
	}

	public String getServerIP() {
		return serverIP;
	}

	public void setServerIP(String destinationIP) {
		this.serverIP = destinationIP;
	}

	public String getClientPort() {
		return clientPort;
	}

	public void setClientPort(String sourcePort) {
		this.clientPort = sourcePort;
	}

	public String getServerPort() {
		return serverPort;
	}

	public void setServerPort(String destinationPort) {
		this.serverPort = destinationPort;
	}
	public String getTransportProtocol() {
		return transportProtocol;
	}

	public void setTransportProtocol(String transportProtocol) {
		this.transportProtocol = transportProtocol;
	}
	
	public String getInternetProtocol() {
		return internetProtocol;
	}

	public void setInternetProtocol(String internetProtocol) {
		this.internetProtocol = internetProtocol;
	}

	public String toString(){
		StringBuilder result = new StringBuilder();
		result.append("{");
//		result.append(application);
//		result.append("\n");
		result.append("\"internetProtocol\": " + "\""+ internetProtocol + "\"");
		result.append(",\n");
		result.append("\"transportProtocol\": " + "\""+ transportProtocol + "\"");
		result.append(",\n");
		result.append("\"clientIP\": " + "\""+ clientIP + "\"");
		result.append(",\n");
		result.append("\"clientPort\": " + "\""+ clientPort + "\"");
		result.append(",\n");
		result.append("\"serverIP\": " + "\""+ serverIP + "\"");
		result.append(",\n");
		result.append("\"serverPort\": " + "\""+ serverPort + "\"");
		result.append(",\n");
		result.append("\"first\": " + "\""+ first + "\"");
		result.append(",\n");
		result.append("\"lastSeen\": " + "\""+ lastSeen + "\"");
		result.append(",\n");
		result.append("\"bytes\": " + "\""+ bytes + "\"");
		result.append(",\n");
//		result.append("\"serverToClient\": " + "\""+ serverToClient.toString() + "\"");
//		result.append(",\n");
//		result.append("\"clientToServer\": " + "\""+ clientToServer.toString() + "\"");
//		result.append(",\n");
		result.append("\"packets\": " + "\""+ packets + "\"");
		result.append("}\n");
		return result.toString();
	}
	public Flow getServerToClient() {
		return serverToClient;
	}
	public void setServerToClient(Flow flow) {
		this.serverToClient = flow;
	}
	public Flow getClientToServer() {
		return clientToServer;
	}
	public void setClientToServer(Flow clientToServer) {
		this.clientToServer = clientToServer;
	}
}

class FlowId{
	private static FlowId instance = null;
	private long id = 0;

	public static FlowId getInstance() {
		if(instance == null) {
			instance = new FlowId();
		}
		return instance;
	}
	
	public long getId(){
		id++;
		return id;
	}
}
