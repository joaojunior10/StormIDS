package monitor.plugins.prototype;

import java.net.ConnectException;
import java.net.InetAddress;
import java.nio.channels.ClosedChannelException;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import monitor.connectors.ChannelSpecification;


//Abstract class that defines the default behaviour of a plugin.
public abstract class SystemResourcePlugin implements Runnable {
    protected Integer period;
    ChannelSpecification channel = null;
    protected JsonObject objToReturn = null;
    private final static int processor = 6;
    public abstract JsonObject getSystemInformation();

    public abstract String topicName();

    protected SystemResourcePlugin(Integer period) {
        this.period = period;
    }

    public void run() {
        try {
            while (true) {
                JsonParser parser = new JsonParser();
                JsonObject jsonObjToSend = getSystemInformation();
                if (jsonObjToSend != null) {
                    if (topicName().equals("NetworkData")) {
                        JsonArray array = jsonObjToSend.getAsJsonArray("packetList");
                        int size = (processor + array.size() - 1) / processor;
                        for (int i = 0; i < processor; i++) {
                            JsonArray mineArray = new JsonArray();
                            for (int j = size * i; j < size * (i + 1); j++) {
                                try {
                                    mineArray.add(array.get(j));
                                } catch (IndexOutOfBoundsException e) {
                                    //ignored
                                }
                            }
                            JsonObject toSend = new JsonObject();
                            toSend.addProperty("topic", topicName());
                            toSend.addProperty("hostname", InetAddress.getLocalHost().getHostName());
                            toSend.add("packetList", mineArray);
                            sendToChannel(toSend.toString());
                        }
                    } else {
                        //Guarantee that every jsonObject contains the topic name and the hostname.
                        jsonObjToSend.addProperty("topic", topicName());
                        jsonObjToSend.addProperty("hostname", InetAddress.getLocalHost().getHostName());
                        sendToChannel(jsonObjToSend.toString());
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

    public void sendToChannel(String obj) {
        try {
            channel.send(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
