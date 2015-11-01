package util.rules;

import java.io.Serializable;

public class Reference implements Serializable{
	public final static String BUGTRAQ = "http://www.securityfocus.com/bid/";
	public final static String CVE = "http://cve.mitre.org/cgi-bin/cvename.cgi?name=";
	public final static String NESSUS = "http://cgi.nessus.org/plugins/dump.php3?id=";
	public final static String ARACHNIDS = "http://www.whitehats.com/info/IDS";
	public final static String MCAFEE = "http://vil.nai.com/vil/dispVirus.asp?virus_k=";
	public final static String URL = "http://";
		
	public static String getURL(String reference){
		int firstComma = reference.indexOf(',');
		String type = reference.substring(0, firstComma).trim();
		String argument = reference.substring(firstComma+1).trim();
		String url = null;
		
		if( type.equalsIgnoreCase("bugtraq") ){
			url = BUGTRAQ + argument;
		}
		else if( type.equalsIgnoreCase("cve") ){
			url = CVE + argument;
		}
		else if( type.equalsIgnoreCase("nessus") ){
			url = NESSUS + argument;
		}
		else if( type.equalsIgnoreCase("arachnids") ){
			url = ARACHNIDS + argument;
		}
		else if( type.equalsIgnoreCase("mcafee") ){
			url = MCAFEE + argument;
		}
		else if( type.equalsIgnoreCase("url") ){
			url = URL + argument;
		}
//		else{
//			throw new SignatureParseException("Reference name (\"" + type + "\" is invalid");
//		}
		return url;
	}
}
