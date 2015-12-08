package util.rules.payload.options;

import util.rules.Operators;

import java.io.Serializable;

public class ByteTest implements Serializable{
	public Integer bytes;
	public Integer operator;
	public boolean negation = false;
	public Integer value;
	public Integer offset; 
	public boolean relative = false;
	public boolean string = false;
	public Integer number_type;
	
	public static final int HEX = 1;
	public static final int DEC = 2;
	public static final int OCT = 3;
	
	public void parse(String option){
		String[] options = option.split(",");
		bytes = Integer.parseInt(options[0].trim());
		if(options[1].contains("!")){
			negation = false;
			operator =  Operators.OPERATORS.get(options[1].substring(1, options[1].length()));
		}else{
			operator = Operators.OPERATORS.get(options[1]);
		}
		value = Integer.parseInt(options[2].trim());
		offset = Integer.parseInt(options[3].trim());
		if(option.contains("relative"))
			relative = true;
		if(option.contains("string"))
			string = true;
		if(option.contains("hex"))
			number_type = HEX;
		if(option.contains("dec"))
			number_type = DEC;
		if(option.contains("oct"))
			number_type = OCT;
	}

	public String toString(){
		StringBuilder result = new StringBuilder();
        String operation = Operators.getOperator(this.operator);
		if(bytes != null)
			result.append(bytes + ", ");
		if(negation)
			result.append("!");
		result.append(operation + ", ");
		if(value != null)
			result.append(value + ", ");
		if(offset != null)
			result.append(offset + ", ");
		if(relative)
			result.append("relative");
		if(string)
			result.append("string");
		if(number_type != null){
			switch(number_type){
				case 1:
					result.append("hex");
					break;
				case 2:
					result.append("dec");
					break;
				case 3:
					result.append("oct");
					break;
			}
		}

		return result.toString();
	}
}
