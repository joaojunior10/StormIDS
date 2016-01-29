package util.rules.header;

import util.rules.Variables;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Header implements Serializable{
	public String action;
	public String protocol;
	public Map<String, Integer> ipsSrc;
	public Map<String, Integer> portsSrc;
	public Map<String, Integer> ipsDst;
	public Map<String, Integer> portsDst;
	public int direction = 0;
	
	private static final int ACTION = 0;
	private static final int PROTOCOL = 1;
	private static final int IP_SRC = 2;
	private static final int PORT_SRC = 3;
	private static final int DIRECTION = 4;
	private static final int IP_DST = 5;
	private static final int PORT_DST = 6;
	
	public static Map<String,Integer> DIRECTIONMAP = new HashMap<String,Integer>();
	static{
		DIRECTIONMAP.put("->", 1);
		DIRECTIONMAP.put("<>", 2);
	}
	public Header(){
		ipsSrc = new HashMap<String, Integer>();
		portsSrc = new HashMap<String, Integer>();
		ipsDst = new HashMap<String, Integer>();
		portsDst = new HashMap<String, Integer>();

	}
	public void parse(String snortRule){
		if( snortRule == null ){
			throw new IllegalArgumentException("Snort rule must not be null");
		}

		if( snortRule.isEmpty() ){
			throw new IllegalArgumentException("Snort rule must not be empty");
		}
		String[] tolkens = snortRule.split(" ");
		action = tolkens[ACTION];
		protocol = tolkens[PROTOCOL];
		ipsSrc = arrayToHash(Variables.ipvars.get(tolkens[IP_SRC]));
		if(ipsSrc == null){
			///TODO log and parse ips not set in variables
			ipsSrc = arrayToHash(parseIps(tolkens[IP_SRC]));
		}
		portsSrc = arrayToHash(Variables.portvars.get(tolkens[PORT_SRC]));
		if(portsSrc == null){
			///TODO log and parse ports not set in variables
			portsSrc = arrayToHash(parsePorts(tolkens[IP_SRC]));
		}
		this.direction = DIRECTIONMAP.get(tolkens[DIRECTION]);

		ipsDst = arrayToHash(Variables.ipvars.get(tolkens[IP_DST]));
		if(ipsDst == null){
			///TODO log and parse ips not set in variables
			ipsDst = arrayToHash(parseIps(tolkens[IP_DST]));
		}
		portsDst = arrayToHash(Variables.portvars.get(tolkens[PORT_DST]));
		if(portsDst == null){
			///TODO log and parse ports not set in variables
			portsDst = arrayToHash(parsePorts(tolkens[PORT_DST]));
		}
	}

	private Map<String, Integer> arrayToHash(String[] strings) {
		if(strings == null)
			return null;
		Map<String, Integer> hashMap = new HashMap<String, Integer>();
		for (String key : strings)
			hashMap.put(key, 1);
		return hashMap;
	}
	private String[] parsePorts(String string) {
		ArrayList<String> result = new ArrayList<>();
		if(string.contains(",")){
			Collections.addAll(result, string.split(","));
		}
		result.add(string);
		return result.toArray(new String[result.size()]);
	}
	private String[] parseIps(String string) {
		ArrayList<String> result = new ArrayList<>();
		result.add(string);
		return result.toArray(new String[result.size()]);
	}
	
	
	public String toString(){
		StringBuilder result = new StringBuilder();
		result.append("Header:\n");  
		if(this.action !=  null)
			result.append("\naction: "+ this.action);  
		if(this.protocol !=  null)
			result.append("\nprotocol: "+ this.protocol);
		result.append("\nIp Source:");  

		result.append("\nIp Source:");  
		if(!this.ipsSrc.isEmpty()){
			for(String ip : ipsSrc.keySet())
				result.append("\n" + ip ); 
		}
		result.append("\nPort Source:");  
		if(!this.portsSrc.isEmpty()){
			for(String port : portsSrc.keySet())
				result.append("\n" + port ); 
		}
		result.append("\nIp Destination:");  
		if(!this.ipsDst.isEmpty()){
			for(String ip : ipsDst.keySet())
				result.append("\n" + ip ); 
		}
		result.append("\nPort Destination:");  
		if(!this.portsDst.isEmpty()){
			for(String port : portsDst.keySet())
				result.append("\n" + port ); 
		}

		if(this.direction !=  0)
			result.append("\ndirection "+ this.direction + "\n");

		return result.toString();
	}

}
