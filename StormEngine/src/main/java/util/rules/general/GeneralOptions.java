package util.rules.general;

import util.rules.Option;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeneralOptions implements Serializable{
	public String msg;
	public List<String> references;
	public Integer gid;
	public Integer sid;
	public Integer rev;
	public String classtype;
	public Integer priority;
	public List<Map<String,String>> metadata;
	public static  Map<String,Integer> GENERALOPTIONS = new HashMap<String,Integer>();
	static{
		GENERALOPTIONS.put("msg", 1);
		GENERALOPTIONS.put("reference", 2);
		GENERALOPTIONS.put("gid", 3);
		GENERALOPTIONS.put("sid", 4);
		GENERALOPTIONS.put("rev", 5);
		GENERALOPTIONS.put("classtype", 6);
		GENERALOPTIONS.put("priority", 7);
		GENERALOPTIONS.put("metadata", 8);

	}
	
	public GeneralOptions(){
		references = new ArrayList<String>();
		metadata = new ArrayList<Map<String,String>>();
	}
	@SuppressWarnings("serial")
	public void parse(Option option){
		switch (GENERALOPTIONS.get(option.getName())){
			case 1:
				this.msg = option.getValue().replaceAll("\"","");
				break;
			case 2:
				this.references.add(Reference.getURL(option.getValue()));
				break;
			case 3:
				this.gid = Integer.parseInt(option.getValue());
				break;
			case 4:
				this.sid = Integer.parseInt(option.getValue());
				break;
			case 5:
				this.rev = Integer.parseInt(option.getValue());
				break;
			case 6:
				this.classtype = option.getValue();
				break;
			case 7:
				this.priority = Integer.parseInt(option.getValue());
				break;
			case 8:
				//TODO extract
				String[] metadata = option.getValue().split(",");
				for(String m : metadata ){
					final String key = m.trim().split(" ",2)[0].trim();
					final String value = m.trim().split(" ",2)[1].trim();
					this.metadata.add(
							new HashMap<String,String>(){{
								put(key,value);
							}}
							);
				}
		}
	}

	public String toString(){
		StringBuilder result = new StringBuilder();
		result.append("\nGeneral Option:\n");  
		if(this.msg !=  null)
			result.append("\nmsg: "+ this.msg);  
		result.append("\nreferences:");  
		if(!this.references.isEmpty()){
			for(String r : references)
				result.append("\n" + r ); 
		}
		if(this.gid !=  null)
			result.append("\ngid: "+ this.gid);
		if(this.sid !=  null)
			result.append("\nsid: "+ this.sid);
		if(this.rev !=  null)
			result.append("\nrev: "+ this.rev);
		if(this.classtype !=  null)
			result.append("\nclasstype: "+ this.classtype);
		if(this.priority !=  null)
			result.append("\npriority: "+ this.priority);
		result.append("\nmetadatas:");  
		if(!this.metadata.isEmpty()){
			for(Map<String, String> m : metadata)
				result.append("\n" + m.toString() ); 
		}
		result.append("\n");
		return result.toString();
	}
}
