package manager.models;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by joao on 7/12/15.
 */
public class Match implements Serializable, Comparator<Match>{
    public String id;
    public Date timelog;
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

    @Override
    public int compare(Match o1, Match o2) {
        return o1.timelog.compareTo(o2.timelog);
    }
}
