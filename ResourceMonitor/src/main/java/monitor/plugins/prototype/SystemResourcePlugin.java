package monitor.plugins.prototype;

import java.net.InetAddress;

import monitor.connectors.ChannelSpecification;
import monitor.util.json.JSONObject;


//Abstract class that defines the default behaviour of a plugin.
public abstract class SystemResourcePlugin implements Runnable {
	protected Integer period;
	ChannelSpecification channel = null;
	protected JSONObject objToReturn = null;
	
	public abstract JSONObject getSystemInformation();
	public abstract String topicName();
	
	protected SystemResourcePlugin(Integer period){
		this.period = period;
	}
	public void run() {
		try {
			while (true){
				JSONObject jsonObjToSend = getSystemInformation();
				//Guarantee that every jsonObject contains the topic name and the hostname.
				jsonObjToSend.put("topic",  topicName());
				jsonObjToSend.put("hostname",  InetAddress.getLocalHost().getHostName());
				if(jsonObjToSend != null)sendToChannel(jsonObjToSend);
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
	
	public void sendToChannel(JSONObject obj) throws Exception {
		channel.send(obj);
	}
}
