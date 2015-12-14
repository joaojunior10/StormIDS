package util.rules.payload.options;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpEncode implements Serializable{
	public Integer http_buffer_type;
	public List<Integer> encoding_types;
	public boolean negation = false;
	//http_buffer_type
	public static final int URI = 1;
	public static final int HEADER = 2;
	public static final int COOKIE = 3;
	//encoding_type
	public static final int UTF8 = 1;
	public static final int DOUBLE_ENCODE = 2;
	public static final int NON_ASCII = 3;
	public static final int UENCODE = 4;
	public static final int BARE_BYTE = 5;
	public static final int ASCII = 6;
	public static final int IIS_ENCODE = 7;

	public static Map<String,Integer> HTTP_BUFFER_TYPE_OPTIONS = new HashMap<String,Integer>();
	static{
		HTTP_BUFFER_TYPE_OPTIONS.put("uri", URI);
		HTTP_BUFFER_TYPE_OPTIONS.put("header", HEADER);
		HTTP_BUFFER_TYPE_OPTIONS.put("cookie", COOKIE);
	}
	public static Map<String,Integer> ENCODING_TYPE_OPTIONS = new HashMap<String,Integer>();
	static{
		ENCODING_TYPE_OPTIONS.put("utf8", UTF8);
		ENCODING_TYPE_OPTIONS.put("double_encode", DOUBLE_ENCODE);
		ENCODING_TYPE_OPTIONS.put("non_ascii", NON_ASCII);
		ENCODING_TYPE_OPTIONS.put("uencode", UENCODE);
		ENCODING_TYPE_OPTIONS.put("bare_byte", BARE_BYTE);
		ENCODING_TYPE_OPTIONS.put("ascii", ASCII);
		ENCODING_TYPE_OPTIONS.put("iis_encode", IIS_ENCODE);
	}
	
	public HttpEncode(){
		encoding_types = new ArrayList<Integer>();
	}
	
	public void parse(String option){
		String[] options = option.split(",");
		this.http_buffer_type = HTTP_BUFFER_TYPE_OPTIONS.get(options[0].trim());
		if(options[1].contains("!")){
			this.negation = true;
			options[1].replace("!", "");
		}
		String[] encodingTypes = options[1].split("|");
		for(String e : encodingTypes){
			this.encoding_types.add(ENCODING_TYPE_OPTIONS.get(e.trim()));
		}
	}
	
	public static String getHttpBufferType(Integer http_buffer_type) {
		String key= null;
		for(Map.Entry entry: HTTP_BUFFER_TYPE_OPTIONS.entrySet()){
            if(http_buffer_type.equals(entry.getValue())){
                key = (String) entry.getKey();
                break;
            }
        }
		return key;
	}
	
	public static String getEncodingType(Integer encoding_type) {
		String key= null;
		for(Map.Entry entry: ENCODING_TYPE_OPTIONS.entrySet()){
            if(encoding_type.equals(entry.getValue())){
                key = (String) entry.getKey();
                break;
            }
        }
		return key;
	}
	
	public String toString(){
		StringBuilder result = new StringBuilder();
		result.append(getHttpBufferType(this.http_buffer_type) + ":");
		for(Integer e : this.encoding_types){
			result.append(getEncodingType(e) + "|");
		}
		return result.toString();
	}
	
}
