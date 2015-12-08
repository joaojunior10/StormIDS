package util.rules.nonpayload;

import util.rules.Operators;

import java.io.Serializable;

public class IType implements Serializable{
	public int operation;
	public int max = 255;
	public int min = 0;
	
	public void setOperation(String operation) {
		this.operation = Operators.OPERATORS.get(operation);
	}
	
	public void parse(String option){
		char firstChar = option.charAt(0);
		if(!Character.isDigit(firstChar)){
			setOperation(firstChar + "");
			max = Integer.parseInt(option.substring(1, option.length()));
		}
		else{
			String[] op = option.split("-");
			min = Integer.parseInt(op[0]);
			setOperation("-");
			max = Integer.parseInt(op[1]);
		}
	}
	
	public String toString(){
		StringBuilder result = new StringBuilder();
        String operation = Operators.getOperator(this.operation);
		result.append("\nitype: ");
		if(min != 0)
			result.append(min);
		result.append(operation);
		if(max != 255)
			result.append(min);
		return result.toString();
	}
}
