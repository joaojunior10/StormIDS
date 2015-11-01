package util.rules;

import java.io.Serializable;

import util.rules.nonpayload.NonPayloadOptions;
import util.rules.payload.PayloadOptions;


public class SnortSignature implements Serializable{
	public int id;
	public Header header;
	public GeneralOptions generalOptions;
	public NonPayloadOptions nonPayloadOptions;
	public PayloadOptions payloadOptions;

	SnortSignature(){
		header = new Header();
		generalOptions = new GeneralOptions();
		nonPayloadOptions = new NonPayloadOptions();
		payloadOptions = new PayloadOptions();
	}
	public void parse(String snortRule) {
		String snortRulesHeader = snortRule.substring(0, snortRule.indexOf('('));
		header.parse(snortRulesHeader);
		
		String[] snortRulesOptions = snortRule.substring((snortRule.indexOf('(') + 1), snortRule.indexOf(')')).split(";");
		
		for(String op : snortRulesOptions){
			Option option = new Option();
			option.parse(op);
			//If it is a general option add to the General options
			if(GeneralOptions.GENERALOPTIONS.containsKey(option.getName())){
				generalOptions.parse(option);
			}
			else if(NonPayloadOptions.NONPAYLOADPTIONS.containsKey(option.getName())){
				nonPayloadOptions.parse(option);
			}
			else if(PayloadOptions.PAYLOADPTIONS.containsKey(option.getName())){
				payloadOptions.parse(option);
			}
		}
		//TODO create regex
	}

	public String toString(){
		StringBuilder result = new StringBuilder();
		if(header != null){
			result.append(header.toString());
		}
		if(generalOptions != null){
			result.append(generalOptions.toString());
		}
		if(nonPayloadOptions != null){
			result.append(nonPayloadOptions.toString());
		}
		return result.toString();
	}
}
