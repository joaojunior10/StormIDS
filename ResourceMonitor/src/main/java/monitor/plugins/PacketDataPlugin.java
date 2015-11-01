package monitor.plugins;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import monitor.plugins.packetcapture.PacketCapture;
import monitor.plugins.packetcapture.PacketQueue;
import monitor.plugins.prototype.SystemResourcePlugin;
import monitor.util.json.JSONObject;
import monitor.util.json.JSONTokener;

public class PacketDataPlugin extends SystemResourcePlugin{
	public PacketDataPlugin(Integer period){
		super(period);
		if(!PacketCapture.getInstance().isAlive())
			PacketCapture.getInstance().start();
	}
	
	@Override
	public JSONObject getSystemInformation() {
		String packets = new String(PacketQueue.getInstance().getPackets());
		//writeFile(packets);
		objToReturn = new JSONObject(new JSONTokener(packets));
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
