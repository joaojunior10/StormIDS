package util.rules.payload.options;

import util.rules.Operators;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Urilen implements Serializable{
	public int operation;
	public Integer max;
	public Integer min;
	public Integer uribuf;
	
	public static final int NORM = 1;
	public static final int RAW = 2;
	
	public static Map<String,Integer> URIBUF_OPTIONS = new HashMap<String,Integer>();
	static{
		URIBUF_OPTIONS.put("norm", NORM);
		URIBUF_OPTIONS.put("raw", RAW);
	}
	public void setOperation(String operation) {
		this.operation = Operators.OPERATORS.get(operation);
	}
	
	public void parse(String option){
		String[] options = option.split(",");
		if(options[1] != null){
			uribuf = URIBUF_OPTIONS.get(options[1]);
		}
		char firstChar = options[0].charAt(0);
		if(!Character.isDigit(firstChar)){
			setOperation(firstChar + "");
			max = Integer.parseInt(option.substring(1, option.length()));
		}
		else{
			String[] op = options[0].split("<>");
			min = Integer.parseInt(op[0]);
			setOperation("-");
			max = Integer.parseInt(op[1]);
		}
	}
	
	public String toString(){
		StringBuilder result = new StringBuilder();
        String operation = Operators.getOperator(this.operation);
		if(min != null)
			result.append(min);
		result.append(operation);
		if(max != null)
			result.append(min);
		if(uribuf != null)
			result.append("," + uribuf);
		return result.toString();
	}
}
