package util.packetdata;

import java.io.Serializable;

public class FlowFlags implements Serializable{
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
}
