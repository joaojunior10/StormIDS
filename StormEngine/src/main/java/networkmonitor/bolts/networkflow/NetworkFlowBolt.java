package networkmonitor.bolts.networkflow;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import networkmonitor.bolts.analyser.Analyser;

import org.apache.log4j.Logger;

import util.json.JSONObject;
import util.rules.Rules;
import util.rules.SnortSignature;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.tuple.Tuple;

public class NetworkFlowBolt extends Analyser{
	private List<SnortSignature> snortSignatures;
	public NetworkFlowBolt(String topic) {
		super(topic);
		Rules rules = new Rules();
		snortSignatures = rules.get();
	}

	/**
	 * UsageAnalyser
	 * Bolt that process very frequent data usage data from a resource.
	 */
	private static final long serialVersionUID = 1L;
	String topic;
	private static final Logger LOG = Logger.getLogger(NetworkFlowBolt.class);

	//Every bolt that implements this class must decide how they want to treat the data.
	@Override
	public void treatData(JSONObject jsonObj, BasicOutputCollector collector) {
		//writeOutputToFile(jsonObj.toString());
		LOG.warn(jsonObj.toString());
	}

	public void execute(Tuple input, BasicOutputCollector collector) {
		JSONObject jsonObj = (JSONObject) input.getValue(0);
		treatData(jsonObj,  collector);
	}

	void writeOutputToFile(String warningMessage){
		String path = "./analytics/NETWORK.txt";       

		//creating file object from given path
		File file = new File(path);

		//FileWriter second argument is for append if its true than FileWritter will
		//write bytes at the end of File (append) rather than beginning of file
		FileWriter fileWriter;
		try {
			fileWriter = new FileWriter(file,true);
			//Use BufferedWriter instead of FileWriter for better performance
			BufferedWriter bufferFileWriter  = new BufferedWriter(fileWriter);
			fileWriter.append(warningMessage+"\n");

			//Don't forget to close Streams or Reader to free FileDescriptor associated with it
			bufferFileWriter.close();

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}


}
