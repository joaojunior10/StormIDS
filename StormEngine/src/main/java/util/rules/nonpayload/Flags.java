package util.rules.nonpayload;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import util.rules.Operators;

public class Flags implements Serializable{
	public int operation;
	public List<String> flags;
	public List<String> ignore;
	
	public Flags(){
		flags = new ArrayList<String>();
		ignore = new ArrayList<String>();
	}
	public void setOperation(String operation) {
		this.operation = Operators.OPERATORS.get(operation);
	}
	
	public void parse(String option){
		char firstChar = option.charAt(0);
		String[] flags;
		if(!Character.isDigit(firstChar)){
			setOperation(firstChar + "");
			flags = option.substring(1, option.length()).split(",");
		}
		else{
			flags = option.substring(0, option.length()).split(",");
		}
		this.flags.addAll(Arrays.asList(flags[0].split("")));
		this.ignore.addAll(Arrays.asList(flags[1].split("")));
	}
	
	public String toString(){
		StringBuilder result = new StringBuilder();
        String operation = Operators.getOperator(this.operation);
		result.append("\nFlags: " + operation );
		if(!this.flags.isEmpty())
			result.append(this.flags);
		if(!this.ignore.isEmpty())
			result.append(this.ignore);
		return result.toString();
	}

}
