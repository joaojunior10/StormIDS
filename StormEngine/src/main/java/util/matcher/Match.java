package util.matcher;

import networkmonitor.bolts.networkdata.PacketData;
import util.rules.general.GeneralOptions;
import util.rules.SnortSignature;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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
    public SnortSignature rule;
    public PacketData packet;

    public Match(){
        this.rule = new SnortSignature();
        this.packet = new PacketData();
    }
    public Match(PacketData packet, SnortSignature rule, String hostname){
        this.timelog = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(Calendar.getInstance().getTime());
        this.hostname = hostname;
        this.sourceIP = packet.sourceIP;
        this.sourcePort = packet.sourcePort;
        this.destinationIP = packet.destinationIP;
        this.destinationPort = packet.destinationPort;
        this.rule = rule;
        this.packet = packet;
    }
}


