package monitor.plugins.prototype;

import java.net.InetAddress;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import monitor.connectors.ChannelSpecification;


//Abstract class that defines the default behaviour of a plugin.
public abstract class SystemResourcePlugin implements Runnable {
	protected Integer period;
	ChannelSpecification channel = null;
	protected JsonObject objToReturn = null;
	
	public abstract JsonObject getSystemInformation();
	public abstract String topicName();
	
	protected SystemResourcePlugin(Integer period){
		this.period = period;
	}
	public void run() {
		try {
			while (true){
				JsonParser parser = new JsonParser();
				JsonObject jsonObjToSend = getSystemInformation();
				if (jsonObjToSend != null) {
					//Guarantee that every jsonObject contains the topic name and the hostname.
					jsonObjToSend.addProperty("topic", topicName());
					jsonObjToSend.addProperty("hostname", InetAddress.getLocalHost().getHostName());
					sendToChannel(jsonObjToSend.toString());
				}
				Thread.sleep(period);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
//	     catch (ClosedChannelException e) {
//			System.err.println("[Resource Monitor] ["+ topicName() +" Plugin] Channel Dropped! :(");
//		} catch(ConnectException e){
//			System.err.println("[Resource Monitor] ["+ topicName() +" Plugin] Connection Dropped! :(");
//		} catch(kafka.common.FailedToSendMessageException e){
//			System.err.println("[Resource Monitor] ["+ topicName() +" Plugin] Failled To Send Message! :(");
//		} catch(kafka.common.KafkaException e){
//				System.err.println("[Resource Monitor] ["+ topicName() +" Plugin] Kafka Problem! :(");
//		}
		catch (Exception e) {
			e.printStackTrace();

			System.err.println("[Resource Monitor] ["+ topicName() +" Plugin] Shutting down... Check the connection to the server! :(");
		}

	}
	public void setChannel(ChannelSpecification channel)
			throws InterruptedException {
		this.channel = channel;

	}
	
	public void sendToChannel(String obj) throws Exception {
		channel.send(obj);
	}
}
