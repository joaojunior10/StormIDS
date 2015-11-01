package networkmonitor.bolts;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import util.json.JSONObject;
import util.json.JSONTokener;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class TesterBolt extends BaseBasicBolt {

	/**
	 * MultiplexerBolt 
	 * Distributes the computation based on the topic.
	 */
	private static final long serialVersionUID = 1L;
	private Long counter = (long) -1;
	private Long time;
	public void execute(Tuple tuple, BasicOutputCollector collector) {
		//time = System.nanoTime();
		counter ++;
		//Every one thousand messages get the system time
//		if (counter <= 5000 && (counter % 1000) == 0) {
//			time = System.nanoTime();
//			writeOutputToFile(counter.toString()+","+time.toString());
//		}
		if (counter == 4999) {
			time = System.nanoTime();
			writeOutputToFile(time.toString());
		}
//		if (counter == 0) {
//			writeOutputToFile(counter.toString()+","+time.toString());
//		}
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declareStream("DefaultStream", new Fields("JsonObject" ) );
	}
	
	void writeOutputToFile(String warningMessage){
		 String path = "./testAnalytics/Storm.txt";       
	      
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
