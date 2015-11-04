package util.rules.payload;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import util.rules.Option;
import util.rules.payload.options.*;

public class PayloadOptions implements Serializable{
	public List<Content> contents;
	public Pattern pattern;
	// handle more than one content
	public Content currentContent;
	public HttpEncode http_encode;
	//TODO handle negation modifiers
	public String uricontent;
	public Urilen urilen;
	public Isdataat istadaa;
	public boolean pkt_data;
	public boolean file_data;
	public Base64Decode base64_decode;
	public boolean base64_data;
	public ByteTest byte_test;
	public ByteJump byte_jump;
	public ByteExtract byte_extract;
	public boolean ftpbounce;
	public Ans1 ans1;
	public Csv csv;
	//TODO protected content
	
	
	public static Map<String,Integer> PAYLOADPTIONS = new HashMap<String,Integer>();
	static{
		PAYLOADPTIONS.put("content", 1);
		PAYLOADPTIONS.put("protected_content", 2);
		PAYLOADPTIONS.put("hash", 3);
		PAYLOADPTIONS.put("length", 4);
		PAYLOADPTIONS.put("nocase", 5);
		PAYLOADPTIONS.put("rawbytes", 6);
		PAYLOADPTIONS.put("depth", 7);
		PAYLOADPTIONS.put("offset", 8);
		PAYLOADPTIONS.put("distance", 9);
		PAYLOADPTIONS.put("within", 10);
		PAYLOADPTIONS.put("http_client_body", 11);
		PAYLOADPTIONS.put("http_cookie", 12);
		PAYLOADPTIONS.put("http_raw_cookie", 13);
		PAYLOADPTIONS.put("http_header", 14);
		PAYLOADPTIONS.put("http_uri", 15);
		PAYLOADPTIONS.put("http_raw_uri", 16);
		PAYLOADPTIONS.put("http_stat_code", 17);
		PAYLOADPTIONS.put("http_stat_msg", 18);
		PAYLOADPTIONS.put("fast_pattern", 19);
		PAYLOADPTIONS.put("uricontent", 20);
		PAYLOADPTIONS.put("urilen", 21);
		PAYLOADPTIONS.put("istadaa", 22);
		PAYLOADPTIONS.put("file_data", 23);
		PAYLOADPTIONS.put("base64_decode", 24);
		PAYLOADPTIONS.put("base64_data", 25);
		PAYLOADPTIONS.put("byte_jump", 26);
		PAYLOADPTIONS.put("byte_extract", 27);
		PAYLOADPTIONS.put("ftpbounce", 28);
		PAYLOADPTIONS.put("ans1", 29);
		PAYLOADPTIONS.put("csv", 30);

	}
	
	public PayloadOptions(){
		contents = new ArrayList<Content>();
	}
	@SuppressWarnings("serial")
	public void parse(Option option)  {
		switch (PAYLOADPTIONS.get(option.getName())){
			case 1:
				currentContent = new Content();
				currentContent.parseContent(option.getValue());
				this.contents.add(currentContent);
				break;
			case 5:
				currentContent.nocase = true;
				currentContent.toNocase();
				break;
			case 6:
				currentContent.rawbytes = true;
				break;
			case 7:
				currentContent.depth = Integer.parseInt(option.getValue());
				break;
			case 8:
				currentContent.offset = Integer.parseInt(option.getValue());
				break;
			case 9:
				currentContent.distance = Integer.parseInt(option.getValue());
				break;
			case 10:
				currentContent.within = Integer.parseInt(option.getValue());
				break;
			case 11:
				currentContent.http_client_body = true;
				break;
			case 12:
				currentContent.http_cookie = true;
				break;
			case 13:
				currentContent.http_raw_cookie = true;
				break;
			case 14:
				currentContent.http_header = true;
				break;
			case 15:
				currentContent.http_uri = true;
				break;
			case 16:
				currentContent.http_raw_uri = true;
				break;
			case 17:
				currentContent.http_stat_code = true;
				break;
			case 18:
				currentContent.http_stat_msg = true;
				break;
			case 19:
				currentContent.fast_pattern.parse(option.getValue());;
				break;
			case 20:
				break;
			case 21:
				break;
		}
	}

    public void process(){

    }
}
