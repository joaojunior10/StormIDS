package monitor.plugins;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import monitor.plugins.packetcapture.ActiveConnections;
import monitor.plugins.packetcapture.PacketCapture;
import monitor.plugins.prototype.SystemResourcePlugin;

public class PacketCapturePlugin extends SystemResourcePlugin {

 
	public PacketCapturePlugin(Integer period){
		super(period);
		if(!PacketCapture.getInstance().isAlive())
			PacketCapture.getInstance().start();
	}
	
	@Override
	public JsonObject getSystemInformation() {
		String activeConnections = new String(ActiveConnections.getInstance().toJSON());
		//writeFile(activeConnections);
		Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		objToReturn =  parser.parse(gson.toJson(activeConnections)).getAsJsonObject();
		return objToReturn;
	}

	@Override
	public String topicName() {
		return new String("NetworkFlow");
	}

	void writeFile(String packets){
		BufferedWriter writer = null;
		try {
			//create a temporary file
			String timeLog = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
			File logFile = new File("text");

			// This will output the full path where the file will be written to...
			System.out.println(logFile.getCanonicalPath());

			writer = new BufferedWriter(new FileWriter(logFile));
			writer.write(packets);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// Close the writer regardless of what happens...
				writer.close();
			} catch (Exception e) {
			}
		}
	}
	/**
	 * Parse the packet and return a json
	 * @param packet
	 * @return
	 */
	private String packetToJson(String packet){
		String[] packetString = packet.split("\n");
		StringBuilder result = new StringBuilder();
		int i;
		int option = 1;
		result.append("{\""+topicName()+"\":[");
		for(i=0;i<packetString.length;i++){
			if(packetString[i].charAt(0) == '[' && !packetString[i].contains("[bytes]")){
				if(i>0){
					result.append("}]},");
				}
				String[] headerString = packetString[i].replace("[", "").replace("]", "").replace(")", "").replace("(", ":").split(":");
				result.append("{\""+headerString[0]+"\":[");
				result.append("{\"Size\":");
				result.append("\""+headerString[1]+"\"");
			}
			else{
				String[] bodyString = packetString[i].split(":",2);//Split the string into 2 substrings
				if(bodyString[0].trim().equals("Option")){
					bodyString[0]=bodyString[0].concat("["+option+"]");
					option++;
				}
				result.append(",\""+bodyString[0].trim()+"\":");
				result.append("\""+bodyString[1].trim()+"\"");
			}
		}
		result.append("}]}]}");
		System.out.println(result.toString());
		return result.toString();
	}
}
