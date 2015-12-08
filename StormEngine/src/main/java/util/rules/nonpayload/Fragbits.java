package util.rules.nonpayload;

import util.rules.Operators;

import java.io.Serializable;

public class Fragbits implements Serializable{
	public int operation;
	public boolean M = false;
	public boolean D = false;
	public boolean R = false;
	
	public void setOperation(String operation) {
		this.operation = Operators.OPERATORS.get(operation);
	}
	
	public void parse(String option){
		if(option.contains("M"))
			this.M = true;
		if(option.contains("D"))
			this.D = true;
		if(option.contains("R"))
			this.R = true;
		if(option.contains("+"))
			setOperation("+");
		else if(option.contains("*"))
			setOperation("*");
		else if(option.contains("!"))
			setOperation("!");
	}
	
	public String toString(){
		StringBuilder result = new StringBuilder();
        String operation = Operators.getOperator(this.operation);
		result.append("\nFragbits: " + operation);
		if(M)
			result.append("M");
		if(D)
			result.append("D");
		if(R)
			result.append("R");
		return result.toString();
	}

}
