package util.rules.nonpayload;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import util.rules.Option;

public class NonPayloadOptions implements Serializable{
	//Create common class for similar options
	public Fragoffset fragoffset;
	public TTL ttl;
	public TOS tos;
	public Integer id;
	public String ipopts;
	public Fragbits fragbits;
	public DSize dsize;
	public Flags flags;
	public Flow flow;
	//TODO private int flowbits;
	public Integer seq;
	public Integer ack;
	public Window window;
	public IType itype;
	//public  icode;
	public Integer icmp_id;
	public Integer icmp_seq;
	//public int rpc;
	//public int ip_proto;
	public boolean sameip;	
	
	public static Map<String,Integer> NONPAYLOADPTIONS = new HashMap<String,Integer>();
	static{
		NONPAYLOADPTIONS.put("fragoffset", 1);
		NONPAYLOADPTIONS.put("ttl", 2);
		NONPAYLOADPTIONS.put("tos", 3);
		NONPAYLOADPTIONS.put("id", 4);
		NONPAYLOADPTIONS.put("ipopts", 5);
		NONPAYLOADPTIONS.put("fragbits", 6);
		NONPAYLOADPTIONS.put("dsize", 7);
		NONPAYLOADPTIONS.put("flags", 8);
		NONPAYLOADPTIONS.put("flow", 9);
		NONPAYLOADPTIONS.put("flowbits", 10);
		NONPAYLOADPTIONS.put("seq", 11);
		NONPAYLOADPTIONS.put("ack", 12);
		NONPAYLOADPTIONS.put("window", 13);
		NONPAYLOADPTIONS.put("itype", 14);
		NONPAYLOADPTIONS.put("icode", 15);
		NONPAYLOADPTIONS.put("icmp_id", 16);
		NONPAYLOADPTIONS.put("icmp_seq", 17);
		NONPAYLOADPTIONS.put("rpc", 18);
		NONPAYLOADPTIONS.put("ip_proto", 19);
		NONPAYLOADPTIONS.put("sameip", 20);
	}
	
	public NonPayloadOptions(){
	}
	
	@SuppressWarnings("serial")
	public void parse(Option option){
		switch (NONPAYLOADPTIONS.get(option.getName())){
			case 1:
				this.fragoffset = new Fragoffset();
				this.fragoffset.parse(option.getValue());
				break;
			case 2:
				this.ttl = new TTL();
				this.ttl.parse(option.getValue());
				break;
			case 3:
				this.tos.parse(option.getValue());
				break;
			case 4:
				this.id = Integer.parseInt(option.getValue());
				break;
			case 5:
				this.ipopts = option.getValue();
				break;
			case 6:
				this.fragbits = new Fragbits();
				this.fragbits.parse(option.getValue());
				break;
			case 7:
				this.dsize = new DSize();
				this.dsize.parse(option.getValue());
				break;
			case 8:
				this.flags = new Flags();
				this.flags.parse(option.getValue());
				break;
			case 9:
				this.flow = new Flow();
				this.flow.parse(option.getValue());
				break;
//			case 10:
//				this.flowbits.parse(option.getValue());
//				break;
			case 11:
				this.seq = Integer.parseInt(option.getValue());
				break;
			case 12:
				this.ack = Integer.parseInt(option.getValue());
				break;
			case 13:
				this.window = new Window();
				this.window.parse(option.getValue());
				break;
			case 14:
				this.itype = new IType();
				this.itype.parse(option.getValue());
				break;
//			case 15:
//				this.ack = Integer.parseInt(option.getValue());
//				break;
			case 16:
				this.icmp_id = Integer.parseInt(option.getValue());
				break;
			case 17:
				this.icmp_seq = Integer.parseInt(option.getValue());
				break;
			case 20:
				this.sameip = true;
		}
	}
	
	public String toString(){
		StringBuilder result = new StringBuilder();
		result.append("\nNon Payload Option:\n");  
		if(this.fragoffset !=  null)
			result.append("\nfragoffset: "+ this.fragoffset.toString());  
		if(this.ttl !=  null)
			result.append("\nttl: "+ this.ttl.toString());
		if(this.tos !=  null)
			result.append("\ntos: "+ this.tos.toString());
		if(this.id !=  null)
			result.append("\nid: "+ this.id.toString());
		if(this.ipopts !=  null)
			result.append("\nipopts: "+ this.ipopts);
		if(this.fragbits !=  null)
			result.append("\nfragbits: "+ this.fragbits.toString());
		if(this.dsize !=  null)
			result.append("\ndsize: "+ this.dsize.toString());
		if(this.flags !=  null)
			result.append("\nflags: "+ this.flags.toString());
		if(this.flow !=  null)
			result.append("\nflow: "+ this.flow.toString());
		if(this.seq !=  null)
			result.append("\nseq: "+ this.seq.toString());
		if(this.ack !=  null)
			result.append("\nack: "+ this.ack.toString());
		if(this.window !=  null)
			result.append("\nwindow: "+ this.window.toString());
		if(this.itype !=  null)
			result.append("\nitype: "+ this.itype.toString());
		if(this.icmp_id !=  null)
			result.append("\nicmp_id: "+ this.icmp_id.toString());
		if(this.icmp_seq !=  null)
			result.append("\nicmp_seq: "+ this.icmp_seq.toString());
		if(this.sameip)
			result.append("\nsameip");
		
		result.append("\n");
		return result.toString();
	}
}
