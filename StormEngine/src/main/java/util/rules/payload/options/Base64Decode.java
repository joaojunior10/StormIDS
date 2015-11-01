package util.rules.payload.options;

import java.io.Serializable;

public class Base64Decode implements Serializable{
	public Integer bytes;
	public Integer offset;
	public boolean relative = false;
	
	public void parse(String option){
		if(option.contains("relative")){
			this.relative = true;
		}
		String[] options = option.split(",");
		this.bytes = Integer.parseInt(options[0].trim().split(" ")[1].trim());
		this.offset = Integer.parseInt(options[1].trim().split(" ")[1].trim());
	}
	
	public String toString(){
		StringBuilder result = new StringBuilder();
		if(this.relative){
			result.append("relative\n");
		}
		if(this.bytes != null){
			result.append(this.bytes + ", ");
		}
		if(this.offset != null){
			result.append(this.relative + "\n");
		}
		
		return result.toString();
	}
}
