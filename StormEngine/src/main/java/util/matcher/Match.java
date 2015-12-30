package util.matcher;
import util.packetdata.PacketData;
import util.rules.SnortSignature;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by joao on 9/6/15.
 */
public class Match implements Serializable {
    public String id;
    public String timelog;
    public String msg;
    public String action;
    public String hostname;
    public String sourceIP;
    public String destinationIP;
    public String sourcePort;
    public String destinationPort;
    public String rule;
    public String packet;

    public Match(){

    }
    public Match(PacketData packet, SnortSignature rule, String hostname){
        this.timelog = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(Calendar.getInstance().getTime());
        this.hostname = hostname;
        this.sourceIP = packet.sourceIP;
        this.sourcePort = packet.sourcePort;
        this.destinationIP = packet.destinationIP;
        this.destinationPort = packet.destinationPort;
        this.msg = rule.generalOptions.msg;
        this.action = rule.header.action;
//        Gson gson = new Gson();
        this.rule = "";
        this.packet = "";
    }
}


