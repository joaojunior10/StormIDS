package networkmonitor.spouts.kafka;

import storm.kafka.SpoutConfig;

public class KafkaSpout extends storm.kafka.KafkaSpout {

	/**
	 * Spout to consume Kafka messages.
	 */
	private static final long serialVersionUID = 4733094086502618705L;

	public KafkaSpout(SpoutConfig spoutConf) {
		super(spoutConf);
	}

}
