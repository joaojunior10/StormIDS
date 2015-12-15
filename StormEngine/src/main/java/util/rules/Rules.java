package util.rules;

import org.apache.commons.io.FileExistsException;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.RuleFiles;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Rules implements Serializable {
    private int count = 0;
    private static final Logger LOG = LoggerFactory.getLogger(Rules.class);

    public Rules() {

    }

    public List<SnortSignature> get() throws URISyntaxException {
        List<SnortSignature> snortSignatures = new ArrayList<SnortSignature>();
        ClassLoader classLoader = getClass().getClassLoader();
        Pattern pattern = Pattern.compile("rules.*");

        try {
            Path path = RuleFiles.getInstance().path;
            Files.walk(path).forEach(filePath -> {
                try {
                    if (filePath.toString().endsWith(".rules")) {
                        Matcher matcher = pattern.matcher(filePath.toString());
                        matcher.find();
                        String group = matcher.group();
                        if(group.equals("rules/rules/web-misc.rules")){
                            count = 1;
                        }
                        BufferedReader txtReader = new BufferedReader(new InputStreamReader(classLoader.getResourceAsStream(group)));
                        readFile(snortSignatures, txtReader);
                    }
                } catch (IOException e) {
                  //  LOG.error(path.toString() + " - " + e.getStackTrace());
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
        //    LOG.error(e.getStackTrace().toString());

            e.printStackTrace();
        }
        LOG.info("Number of rules: " + snortSignatures.size());

        return snortSignatures;

    }

    private void readFile(List<SnortSignature> snortSignatures, BufferedReader file) throws IOException {

        StringBuilder rulesLogger = new StringBuilder();
        String rule;
        while ((rule = file.readLine()) != null) {
            if(!rule.isEmpty() && rule.charAt(0) != '#') {
                SnortSignature snortSignature = new SnortSignature();
                try {
                    snortSignature.parse(rule);
                    if(count == 1){
                        snortSignature.payloadOptions.ftpbounce = true;
                        count = 0;
                    }
                    snortSignatures.add(snortSignature);
//                    rulesLogger.append(snortSignature.toString()).append("\n");
                } catch (Exception e) {
                    //LOG.error(rule + " - " + e.getStackTrace());
                    //LOG.error(rule + "\n");
                }
            }
        }
    }
}

