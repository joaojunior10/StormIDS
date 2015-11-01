package util.rules.nonpayload;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Flow implements Serializable{
	public static final int TO_CLIENT = 1;
	public static final int TO_SERVER = 2;
	public static final int FROM_CLIENT = 3;
	public static final int FROM_SERVER = 4;
	public static final int ESTABLISHED = 5;
	public static final int NOT_ESTABLISHED = 6;
	public static final int STATELESS = 7;
	public static final int NO_STREAM = 8;
	public static final int ONLY_STREAM = 9;
	public static final int NO_FLAG = 10;
	public static final int ONLY_FLAG = 11;

	public List<Integer> flows;

	public static Map<String,Integer> FLOW_OPTIONS = new HashMap<String,Integer>();
	static{
		FLOW_OPTIONS.put("to_client", TO_CLIENT);
		FLOW_OPTIONS.put("to_server", TO_SERVER);
		FLOW_OPTIONS.put("from_client", FROM_CLIENT);
		FLOW_OPTIONS.put("from_server", FROM_SERVER);
		FLOW_OPTIONS.put("established", ESTABLISHED);
		FLOW_OPTIONS.put("not_established", NOT_ESTABLISHED);
		FLOW_OPTIONS.put("stateless", STATELESS);
		FLOW_OPTIONS.put("no_stream", NO_STREAM);
		FLOW_OPTIONS.put("only_stream", ONLY_STREAM);
		FLOW_OPTIONS.put("no_frag", NO_FLAG);
		FLOW_OPTIONS.put("only_frag", ONLY_STREAM);
	}
	
	public Flow(){
		this.flows = new ArrayList<Integer>();
	}
	
	public static String getFlow(Integer flow) {
		String key= null;
		for(Map.Entry entry: FLOW_OPTIONS.entrySet()){
            if(flow.equals(entry.getValue())){
                key = (String) entry.getKey();
                break;
            }
        }
		return key;
	}

	public void parse(String option){
		String[] flows = option.split(",");
		for(String f : flows){
			try {
				this.flows.add(FLOW_OPTIONS.get(f.trim()));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.err.println(option);
			}
		}
	}
	
	public String toString(){
		StringBuilder result = new StringBuilder();
		for(Integer f : this.flows){
	        String flow = getFlow(f);
			result.append(flow + "\t");
		}
		return result.toString();
	}
}
