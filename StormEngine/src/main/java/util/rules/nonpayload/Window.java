package util.rules.nonpayload;

import java.io.Serializable;

import util.rules.Operators;

public class Window implements Serializable{
	public int operation;
	public int window;
	
	public void setOperation(String operation) {
		this.operation = Operators.OPERATORS.get(operation);
	}
	
	public void parse(String option){
		char firstChar = option.charAt(0);
		if(!Character.isDigit(firstChar)){
			setOperation(firstChar + "");
			window = Integer.parseInt(option.substring(1, option.length()));
		}
		else{
			window = Integer.parseInt(option.substring(0, option.length()));
		}
	}
	
	public String toString(){
		StringBuilder result = new StringBuilder();
        String operation = Operators.getOperator(this.operation);
		result.append("\ntos: " + operation + window);
		
		return result.toString();
	}
}
