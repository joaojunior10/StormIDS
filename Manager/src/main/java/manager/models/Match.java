package manager.models;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by joao on 7/12/15.
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
}
