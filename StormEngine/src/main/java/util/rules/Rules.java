package util.rules;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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
                        //System.out.println(count);
                    } catch (IOException e) {
                        //System.out.println(count);
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
                    //count++;
                    //System.out.println(count);
                }
            });
            //Dump the results to a text file and print out rules that didn't match the regex.
        }
    }
}

