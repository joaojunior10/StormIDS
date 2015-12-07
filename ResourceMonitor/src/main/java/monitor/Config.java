package monitor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by joao on 4/11/15.
 */
public class Config {
    private static Config instance = null;
    public String kafkaZooKeeper;
    public String kafkaBroker = "";
    public String bfp;

    protected Config() {
        try {
            String filename = "rm.config";
            ClassLoader classLoader = getClass().getClassLoader();
            Path path = Paths.get(classLoader.getResource(filename).getPath());
            List<String> configList = Files.readAllLines(path);
            for (String config : configList){
                String[] options = config.split(":",2);
                if(options[0].equals("kafka-zookeeper")){
                    kafkaZooKeeper = options[1];
                }else if(options[0].equals("kafka-broker")){
                    kafkaBroker = kafkaBroker.concat(options[1] + ",");
                }else if(options[0].equals("BPF")){
                    bfp = options[1];
                }
            }
            kafkaBroker = kafkaBroker.substring(0,kafkaBroker.length()-1);
        } catch (IOException e) {
            System.err.println("File not found");
        }


    }
    public static Config getInstance() {
        if(instance == null) {
            instance = new Config();
        }
        return instance;
    }
}