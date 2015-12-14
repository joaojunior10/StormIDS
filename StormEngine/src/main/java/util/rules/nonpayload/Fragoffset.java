package util.rules.nonpayload;

import util.rules.Operators;

import java.io.Serializable;

public class Fragoffset implements Serializable{
	public int operation;
	public int fragoffset;
	
	public void setOperation(String operation) {
		this.operation = Operators.OPERATORS.get(operation);
	}
	public void parse(String option){
		char firstChar = option.charAt(0);
		if(!Character.isDigit(firstChar)){
			setOperation(firstChar + "");
			fragoffset = Integer.parseInt(option.substring(1, option.length()));
		}
		else{
			fragoffset = Integer.parseInt(option.substring(0, option.length()));
		}
	}
	
	public String toString(){
		StringBuilder result = new StringBuilder();
        String operation = Operators.getOperator(this.operation);
		result.append("\nFragoffset: " + operation + fragoffset);
		
		return result.toString();
	}
	
}
