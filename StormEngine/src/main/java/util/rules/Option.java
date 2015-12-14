package util.rules;

import java.io.Serializable;

public class Option implements Serializable{

	private String name;
	private String value = null;

	public Option( ){
	}

	public Option( String name, String value ){
		this.name = name;
		this.value = value;
	}

	public String getName(){
		return name;
	}

	public String getValue(){
		return value;
	}

	public boolean hasValue(){
		if( value == null )
			return false;
		else
			return true;
	}
	
	public void parse(String snortRule){
		if( snortRule == null ){
			throw new IllegalArgumentException("Snort rule must not be null");
		}

		if( snortRule.isEmpty() ){
			throw new IllegalArgumentException("Snort rule must not be empty");
		}
		try{
			String[] tolkens = snortRule.split(":");
			this.name = tolkens[0].trim().toLowerCase();
			if(tolkens.length > 1)
				this.value = tolkens[1];
		}catch(ArrayIndexOutOfBoundsException ex){
			//TODO logger
			System.err.println(ex + " - " + this.name + " : " + this.value);
		}
		
		
	}
}
