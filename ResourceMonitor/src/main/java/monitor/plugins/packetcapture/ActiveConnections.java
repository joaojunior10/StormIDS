package monitor.plugins.packetcapture;

import java.util.HashMap;

public class ActiveConnections {
	private HashMap<String,Connection> activeConnections = new HashMap<String,Connection>();
	private static ActiveConnections instance = null;

	protected ActiveConnections() {
	}

	public synchronized static ActiveConnections getInstance() {
		if(instance == null) {
			instance = new ActiveConnections();
		}
		return instance;
	}

	public HashMap<String, Connection> getActiveConnections() {
		return activeConnections;
	}

	public void setActiveConnections(HashMap<String, Connection> activeConnections) {
		this.activeConnections = activeConnections;
	}

	public synchronized boolean addConnection(Connection connection) {
		//TODO change to server and client
		String key1 = new String(connection.serverToClient.sourceIP + connection.serverToClient.sourcePort +
				connection.serverToClient.destinationIP + connection.serverToClient.destinationPort);
		if(activeConnections.containsKey(key1)){
			activeConnections.get(key1).getClientToServer().setBytes(connection.getClientToServer().getBytes());
			activeConnections.get(key1).getClientToServer().setPackets();
			updateConnection(connection, activeConnections.get(key1));
			return true;
		}
		String key2 = new String(connection.getServerToClient().getDestinationIP() + connection.getServerToClient().getDestinationPort()+
				connection.getServerToClient().getSourceIP() + connection.getServerToClient().getSourcePort());
		if(activeConnections.containsKey(key2)){
			activeConnections.get(key2).setServerToClient(connection.getClientToServer());
			activeConnections.get(key2).getServerToClient().setBytes(connection.getClientToServer().getBytes());
			activeConnections.get(key2).getServerToClient().setPackets();
			updateConnection(connection, activeConnections.get(key2));
			return false;
		}
		else{
			connection.setFirst(connection.getLastSeen());
			activeConnections.put(key1,connection);
			return true;
		}
	}

	private synchronized void updateConnection(Connection connection, Connection c) {
		c.setBytes(connection.getBytes());
		c.setPackets();
		c.setLastSeen(connection.getLastSeen());
		//TODO log here to see differences
	}

	public synchronized String toJSON() {
		StringBuilder result = new StringBuilder();
		result.append("{\"PacketCapture\":[\n");
		for(Connection c : activeConnections.values()){
			result.append(c.toString());
			result.append(",\n\n");
		}
		//Remove last comma
		if(!activeConnections.isEmpty())
			result.deleteCharAt(result.length() -3);
		result.append("\n]}\n");
		return result.toString();
	}

}
