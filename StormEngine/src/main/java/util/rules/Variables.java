package util.rules;

import java.io.Serializable;
import java.util.HashMap;

public class Variables implements Serializable{
	//TODO Read variables from config file
	public static HashMap<String,String[]> ipvars = new HashMap<String,String[]>();
	public static HashMap<String,String[]> portvars = new HashMap<String,String[]>();

	static{
		ipvars.put("$HOME_NET", new String[]{"10.0.1.5","10.0.1.13"});
		ipvars.put("$EXTERNAL_NET", new String[]{"any"});
		ipvars.put("$HTTP_SERVERS", new String[]{"10.0.1.5"});
		ipvars.put("$SSH_PORTS", new String[]{"22,1234"});
		ipvars.put("any", new String[]{"any"});
		portvars.put("$HTTP_PORTS", new String[]{"36","80","81","82","83","84","85","86","87","88","89","90","311","383","555","591","593","631",
				"801","808","818","901","972","1158","1220","1414","1533","1741","1830","1942","2231","2301","2381","2578","2809","2980",
				"3029","3037","3057","3128","3443","3702","4000","4343","4848","5000","5117","5250","5600","6080","6173","6988","7000",
				"7001","7071","7144","7145","7510","7770","7777","7778","7779","8000","8008","8014","8028","8080","8081","8082","8085",
				"8088","8090","8118","8123","8180","8181","8222","8243","8280","8300","8333","8344","8500","8509","8800","8888","8899","8983",
				"9000","9060","9080","9090","9091","9111","9290","9443","9999","10000","11371","12601","13014","15489","29991","33300","34412",
				"34443","34444","41080","44449","50000","50002","51423","53331","55252","55555","56712"});
		portvars.put("any", new String[]{"any"});

	}
	
	public Variables(){
	}

}
