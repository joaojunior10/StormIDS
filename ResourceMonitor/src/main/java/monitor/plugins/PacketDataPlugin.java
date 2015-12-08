package monitor.plugins;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import monitor.plugins.packetcapture.PacketCapture;
import monitor.plugins.packetcapture.PacketQueue;
import monitor.plugins.prototype.SystemResourcePlugin;

public class PacketDataPlugin extends SystemResourcePlugin{
	public PacketDataPlugin(Integer period){
		super(period);
		if(!PacketCapture.getInstance().isAlive())
			PacketCapture.getInstance().start();
	}
	
	@Override
	public JsonObject getSystemInformation() {
		String packets = PacketQueue.getInstance().getPackets();
		if(packets == null) return null;
		//writeFile(packets);
		JsonParser parser = new JsonParser();
		objToReturn = parser.parse(packets).getAsJsonObject();
		return objToReturn;
	}

	@Override
	public String topicName() {
		return new String("NetworkData");
	}

	void writeFile(String packets){
		BufferedWriter writer = null;
		try {
			//create a temporary file
			String timeLog = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
			File logFile = new File("getPackets");

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
}
