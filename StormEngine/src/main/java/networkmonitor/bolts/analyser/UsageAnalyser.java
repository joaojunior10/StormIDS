package networkmonitor.bolts.analyser;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import org.apache.log4j.Logger;
import scala.collection.mutable.StringBuilder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class UsageAnalyser  extends BaseBasicBolt {

	/**
	 * UsageAnalyser
	 * Bolt that process very frequent data usage data from a resource.
	 */
	private static final long serialVersionUID = 1L;
	String topic;
	private static final Logger LOG = Logger.getLogger(UsageAnalyser.class);

	//Every bolt that implements this class must decide how they want to treat the data.
	public  void treatData(String hostname, Double value,  BasicOutputCollector collector){
		if (value >= 0.7){
			StringBuilder warningMessage = new StringBuilder("[" + hostname + "] ["+System.currentTimeMillis()+"] ["+ topic +"] is over the safe limit: " + (value*100) +"%.");
			System.err.println(warningMessage);
			writeOutputToFile(warningMessage.toString());
			LOG.warn(warningMessage.toString());
		}
	}

	public UsageAnalyser(String topic){
		this.topic = topic;
	}

	public void execute(Tuple input, BasicOutputCollector collector) {
		String hostname = (String) input.getValue(0);
		Double value = input.getDouble(1);
		treatData(hostname, value,  collector);
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("treatedMessage"));

	}

	String getTopic(){
		return topic;
	}

	void writeOutputToFile(String warningMessage){
		String path = "./analytics/WARNINGS_LOG.txt";       

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
