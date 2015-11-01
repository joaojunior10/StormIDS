package util.rules;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class Rules implements Serializable{
	
	public Rules(){

	}

	public List<SnortSignature> get() {
		ClassLoader classLoader = getClass().getClassLoader();

		String filename = "rules/test.rules";
		List<SnortSignature> snortSignatures = new ArrayList<SnortSignature>();
		File file = new File(classLoader.getResource(filename).getFile());

		if (file.exists()) {
			StringBuilder rulesLogger = new StringBuilder();
			try {
				List<String> snortRules = FileUtils.readLines(file);
				//Iterate over all of the lines in the .rules file
				for (String rule : snortRules) {
					if (!rule.isEmpty() && rule.charAt(0) != '#') {
						SnortSignature snortSignature = new SnortSignature();
						snortSignature.parse(rule);
						snortSignatures.add(snortSignature);
						rulesLogger.append(snortSignature.toString()).append("\n");
					}
				}
				//Dump the results to a text file and print out rules that didn't match the regex.
				System.out.print(rulesLogger.toString());

			} catch (IOException e) {
				e.printStackTrace(System.out);
			}
			//writeOutputToFile(rulesLogger.toString());
		}
		return snortSignatures;
		
	}
	
	static void writeOutputToFile(String warningMessage){
		String path = "./analytics/ParseLog.txt";       

		//creating file object from given path
		File file = new File(path);

		//FileWriter second argument is for append if its true than FileWritter will
		//write bytes at the end of File (append) rather than beginning of file
		FileWriter fileWriter;
		try {
			fileWriter = new FileWriter(file,false);
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

