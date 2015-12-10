package util.rules;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Rules implements Serializable {
    private int count = 0;
    private static final Logger LOG = LoggerFactory.getLogger("tracesLogger");

    public Rules() {

    }

    public List<SnortSignature> get() throws URISyntaxException {
        List<SnortSignature> snortSignatures = new ArrayList<SnortSignature>();
        ClassLoader classLoader = getClass().getClassLoader();
        try {
            URI uri = classLoader.getResource("rules").toURI();
            LOG.trace(uri.toString());
            Path myPath;
            if (uri.getScheme().equals("jar")) {
                java.nio.file.FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.<String, Object>emptyMap());
                myPath = fileSystem.getPath("/rules");
                LOG.trace(myPath.toString());
            } else {
                myPath = Paths.get(uri);
                LOG.trace(myPath.toString());
            }
            Files.walk(myPath).forEach(filePath -> {
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
                    LOG.error(file.getPath());
                    LOG.error(rule + "\n");
                }
            });
            //Dump the results to a text file and print out rules that didn't match the regex.
        }
    }
}

