package util.rules.payload.options;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Isdataat implements Serializable{
	public Integer location;
	public Integer lookingfor;
	public boolean negation = false;
	public static final int RELATIVE = 1;
	public static final int RAWBYTES = 2;
	
	public static Map<String,Integer> LOOKING_FOR_OPTIONS = new HashMap<String,Integer>();
	static{
		LOOKING_FOR_OPTIONS.put("relative", RELATIVE);
		LOOKING_FOR_OPTIONS.put("rawbytes", RAWBYTES);
	}
	public void parse(String option){
		if(option.contains("!")){
			this.negation = true;
			option.replace("!", "");
		}
		String[] options = option.split(",");
		this.location = Integer.parseInt(options[0].trim());
		this.location = LOOKING_FOR_OPTIONS.get(options[1].trim());
	}
	
	public String toString(){
		StringBuilder result = new StringBuilder();
		if(this.negation){
			result.append("!");
		}
		if(this.location != null){
			result.append(this.location + ", ");
		}
		if(this.lookingfor != null){
			result.append(this.lookingfor + "\n");
		}
		
		return result.toString();
	}}
