package monitor.plugins.prototype;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;
import monitor.connectors.ChannelSpecification;
import util.Response;
import util.packetdata.PacketData;


//Abstract class that defines the default behaviour of a plugin.
public abstract class SystemResourcePlugin implements Runnable {
    protected Integer period;
    ChannelSpecification channel = null;
    protected JsonObject objToReturn = null;
    private final static int processor = 6;
    public abstract Object getSystemInformation();

    public abstract String topicName();

    protected SystemResourcePlugin(Integer period) {
        this.period = period;
    }

    public void run() {
        try {
            while (true) {
                List<PacketData> packets = (List<PacketData>) getSystemInformation();
                if (packets != null) {
                    if (topicName().equals("NetworkData")) {
                        int size = (processor + packets.size() - 1) / processor;
                        for (int i = 0; i < processor; i++) {
                            List<PacketData> mineArray = new ArrayList<PacketData>();
                            for (int j = size * i; j < size * (i + 1); j++) {
                                try {
                                    mineArray.add(packets.get(j));
                                } catch (IndexOutOfBoundsException e) {
                                    //ignored
                                }
                            }
                            Response response = new Response(mineArray,InetAddress.getLocalHost().getHostName(),topicName());
                            sendToChannel(response);
                        }
                    } else {
                        //Guarantee that every jsonObject contains the topic name and the hostname.
//                        jsonObjToSend.addProperty("topic", topicName());
//                        jsonObjToSend.addProperty("hostname", InetAddress.getLocalHost().getHostName());
//                        sendToChannel(jsonObjToSend.toString());
                    }
                }
                Thread.sleep(period);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
			System.err.println("[Resource Monitor] ["+ topicName() +" Plugin] Failled To Send Message! :(");
		} catch(kafka.common.KafkaException e){
            e.printStackTrace();
            System.err.println("[Resource Monitor] ["+ topicName() +" Plugin] Kafka Problem! :(");
		}
        catch (Exception e) {
            e.printStackTrace();
            System.err.println("[Resource Monitor] [" + topicName() + " Plugin] Shutting down... Check the connection to the server! :(");
        }
    }
    public void setChannel(ChannelSpecification channel)
            throws InterruptedException {
        this.channel = channel;
    }

    public void sendToChannel(Response obj) {
        try {
            channel.send(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
