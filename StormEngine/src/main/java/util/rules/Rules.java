package util.rules;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class Rules implements Serializable {
    private int count = 0;
    public Rules() {

    }

    public List<SnortSignature> get() {
        String filename = "rules/test.rules";
        List<SnortSignature> snortSignatures = new ArrayList<SnortSignature>();
        ClassLoader classLoader = getClass().getClassLoader();
        try {
            Files.walk(Paths.get(classLoader.getResource("rules").getPath())).forEach(filePath -> {
                if (Files.isRegularFile(filePath)) {
                    File file = new File(filePath.toString());
                    try {
                        readFile(snortSignatures, file);
                        System.out.println(count);
                    } catch (IOException e) {
                        System.out.println(count);
                        e.printStackTrace();

                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return snortSignatures;

    }

    private void readFile(List<SnortSignature> snortSignatures, File file) throws IOException {
        if (file.exists()) {

            StringBuilder rulesLogger = new StringBuilder();
            List<String> snortRules = FileUtils.readLines(file);
            //Iterate over all of the lines in the .rules file
            snortRules.stream().filter(rule -> !rule.isEmpty() && rule.charAt(0) != '#').forEach(rule -> {
                SnortSignature snortSignature = new SnortSignature();
                try {
                    snortSignature.parse(rule);
                    snortSignatures.add(snortSignature);
                    rulesLogger.append(snortSignature.toString()).append("\n");
                } catch (Exception e) {
                    System.out.println(rule);
                    System.out.println(file.getPath());

                    count++;
                    System.out.println(count);
                }
            });
            //Dump the results to a text file and print out rules that didn't match the regex.
        }
    }

    static void writeOutputToFile(String warningMessage) {
        String path = "./analytics/ParseLog.txt";

        //creating file object from given path
        File file = new File(path);

        //FileWriter second argument is for append if its true than FileWritter will
        //write bytes at the end of File (append) rather than beginning of file
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(file, false);
            //Use BufferedWriter instead of FileWriter for better performance
            BufferedWriter bufferFileWriter = new BufferedWriter(fileWriter);
            fileWriter.append(warningMessage + "\n");

            //Don't forget to close Streams or Reader to free FileDescriptor associated with it
            bufferFileWriter.close();

        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

    }
}

