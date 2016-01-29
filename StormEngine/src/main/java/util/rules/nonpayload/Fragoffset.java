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
		fragoffset = Integer.parseInt(option.replaceAll("[^\\d]", ""));
		String op = option.replaceAll("[\\d]", "");
		if(!op.isEmpty())
			this.operation = Operators.OPERATORS.get(op);
	}
	
	public String toString(){
		StringBuilder result = new StringBuilder();
        String operation = Operators.getOperator(this.operation);
		result.append("\nFragoffset: " + operation + fragoffset);
		
		return result.toString();
	}
	
}
