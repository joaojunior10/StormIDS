package util.rules;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Operators implements Serializable{
	public static final int NEGATION = 1;
	public static final int LESSTHAN = 2;
	public static final int GREATERTHAN = 3;
	public static final int EQUAL = 4;
	public static final int LESSOREQUALTHAN = 5;
	public static final int GREATEROREQUALTHAN = 6;
	public static final int BETWEEN = 7;
	public static final int MATCHANY = 8;
	public static final int MATCH = 9;
	public static final int AND = 10;
	public static final int OR = 11;


	public static Map<String,Integer> OPERATORS = new HashMap<String,Integer>();
	static{
		OPERATORS.put("!", NEGATION);
		OPERATORS.put(">", LESSTHAN);
		OPERATORS.put("<", GREATERTHAN);
		OPERATORS.put("=", EQUAL);
		OPERATORS.put("<=", LESSOREQUALTHAN);
		OPERATORS.put(">=", GREATEROREQUALTHAN);
		OPERATORS.put("-", BETWEEN);
		OPERATORS.put("+", MATCH);
		OPERATORS.put("*", MATCHANY);
		OPERATORS.put("&", AND);
		OPERATORS.put("^", OR);
	}
	
	public static String getOperator(Integer operation) {
		String key= null;
		for(Map.Entry entry: Operators.OPERATORS.entrySet()){
            if(operation.equals(entry.getValue())){
                key = (String) entry.getKey();
                break;
            }
        }
		return key;
	}

	public static boolean Compare(int operation,int base, int value){
		switch (operation){
			case NEGATION:
				return value != base;
			case LESSTHAN:
				return value < base;
			case GREATERTHAN:
				return value > base;
		}
		return false;
	}
}
