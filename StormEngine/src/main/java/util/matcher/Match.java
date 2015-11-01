package util.matcher;

import backtype.storm.task.OutputCollector;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import com.datastax.driver.core.exceptions.DriverException;
import com.github.fhuss.storm.cassandra.BaseExecutionResultHandler;
import networkmonitor.bolts.networkdata.PacketData;
import util.rules.GeneralOptions;
import util.rules.SnortSignature;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by joao on 9/6/15.
 */
public class Match implements Serializable {
    public String timelog;
    public String hostname;
    public String sourceIP;
    public String destinationIP;
    public String sourcePort;
    public String destinationPort;
    public GeneralOptions generalOptions;
    public PacketData packet;

    public Match(){

    }
    public Match(PacketData packet, SnortSignature rule, String hostname){
        this.timelog = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(Calendar.getInstance().getTime());
        this.hostname = hostname;
        this.sourceIP = packet.sourceIP;
        this.sourcePort = packet.sourcePort;
        this.destinationIP = packet.destinationIP;
        this.destinationPort = packet.destinationPort;
        this.generalOptions = rule.generalOptions;
        this.packet = packet;
    }
}


