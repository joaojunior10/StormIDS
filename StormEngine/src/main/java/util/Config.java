package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by joao on 10/12/15.
 */
public class Config {
    private static Config instance = null;
    public String kafkaZooKeeper;
    public String cassandraKeyspace;
    public String cassandraAddress;

    protected Config() {
        try {
            String filename = "stormids.config";
            ClassLoader classLoader = getClass().getClassLoader();
            BufferedReader txtReader = new BufferedReader(new InputStreamReader(classLoader.getResourceAsStream(filename)));
            String config;
            while ((config = txtReader.readLine()) != null){
                String[] options = config.split(":",2);
                if(options[0].equals("cassandra.keyspace")){
                    cassandraKeyspace = options[1];
                }else if(options[0].equals("cassandra.address")){
                    cassandraAddress = options[1];
                }else if(options[0].equals("kafka.zookeper")){
                    kafkaZooKeeper = options[1];
                }
            }
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
