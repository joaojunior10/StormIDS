package util.rules.payload.options;

import java.io.Serializable;

public class FastPattern implements Serializable{
	public boolean only = false;
	public Integer offset;
	public Integer length;
	
	public void parse(String option){
		if(option.contains("only")){
			this.only = true;
			return;
		}
		String[] options = option.split(",");
		this.offset = Integer.parseInt(options[0].trim());
		this.length = Integer.parseInt(options[1].trim());
	}
	
	public String toString(){
		StringBuilder result = new StringBuilder();
		if(this.only){
			result.append("only\n");
		}
		if(this.offset != null){
			result.append(this.offset + ", ");
		}
		if(this.length != null){
			result.append(this.length + "\n");
		}
		
		return result.toString();
	}
}
