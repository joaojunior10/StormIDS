package monitor.plugins.packetcapture.packetdata;

import java.io.Serializable;

public class Fragbits implements Serializable {
	public boolean M;//More Fragments
	public boolean D;//Don’t Fragment
	public boolean R;//Reserved Bit
}
