package networkmonitor.bolts.slidingwindowcounter.tools;

import backtype.storm.Config;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import org.apache.log4j.Logger;
import util.json.JSONObject;
import util.storm.TupleHelpers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * This bolt performs rolling counts of incoming objects, i.e. sliding window based counting.
 *
 * The bolt is configured by two parameters, the length of the sliding window in seconds (which influences the output
 * data of the bolt, i.e. how it will count objects) and the emit frequency in seconds (which influences how often the
 * bolt will output the latest window counts). For instance, if the window length is set to an equivalent of five
 * minutes and the emit frequency to one minute, then the bolt will output the latest five-minute sliding window every
 * minute.
 *
 * The bolt emits a rolling count tuple per object, consisting of the object itself, its latest rolling count, and the
 * actual duration of the sliding window. The latter is included in case the expected sliding window length (as
 * configured by the user) is different from the actual length, e.g. due to high system load. Note that the actual
 * window length is tracked and calculated for the window, and not individually for each object within a window.
 *
 * Note: During the startup phase you will usually observe that the bolt warns you about the actual sliding window
 * length being smaller than the expected length. This behavior is expected and is caused by the way the sliding window
 * counts are initially "loaded up". You can safely ignore this warning during startup (e.g. you will see this warning
 * during the first ~ five minutes of startup time if the window length is set to five minutes).
 *
 */

public abstract class AbstractSlidingWindowCounterBolt extends BaseRichBolt {
    
    private static final long serialVersionUID = 1737333623611998222L;
    private static final Logger LOG = Logger.getLogger(AbstractSlidingWindowCounterBolt.class);
    private static final int NUM_WINDOW_CHUNKS = 5;
    private static final int DEFAULT_SLIDING_WINDOW_LENGTH_IN_SECONDS = NUM_WINDOW_CHUNKS * 20;
    private static final int DEFAULT_EMIT_FREQUENCY_IN_SECONDS = 5;
    protected static final String WINDOW_LENGTH_WARNING_TEMPLATE = "Actual window length is %d seconds when it should be %d seconds"
    + " (you can safely ignore this warning during the startup phase)";
    
    protected final SlidingWindowCounter<Object> counter;
    protected final int windowLengthInSeconds;
    protected final int emitFrequencyInSeconds;
    protected OutputCollector collector;
    protected NthLastModifiedTimeTracker lastModifiedTracker;
    
    protected abstract void countObj(JSONObject jsonObj);
    protected String topic;
    
    public AbstractSlidingWindowCounterBolt(String topic) {
        this(topic,DEFAULT_SLIDING_WINDOW_LENGTH_IN_SECONDS, DEFAULT_EMIT_FREQUENCY_IN_SECONDS);
    }
    
    public AbstractSlidingWindowCounterBolt(String topic, int windowLengthInSeconds, int emitFrequencyInSeconds) {
        this.topic = topic;
    	this.windowLengthInSeconds = windowLengthInSeconds;
        this.emitFrequencyInSeconds = emitFrequencyInSeconds;
        counter = new SlidingWindowCounter<Object>(deriveNumWindowChunksFrom(this.windowLengthInSeconds,
                                                                             this.emitFrequencyInSeconds));
    }
    
    private int deriveNumWindowChunksFrom(int windowLengthInSeconds, int windowUpdateFrequencyInSeconds) {
        return Math.round(windowLengthInSeconds / windowUpdateFrequencyInSeconds);
    }
    
    @SuppressWarnings("rawtypes")
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
        lastModifiedTracker = new NthLastModifiedTimeTracker(deriveNumWindowChunksFrom(this.windowLengthInSeconds,
                                                                                       this.emitFrequencyInSeconds));
    }
    
    public void execute(Tuple tuple) {
        if (TupleHelpers.isTickTuple(tuple)) {
            LOG.info("Received tick tuple, triggering emit of current window counts");
            emitCurrentWindowCounts();
        }
        else {      	
            countObj((JSONObject) tuple.getValue(0));
            collector.ack(tuple);
        }
    }
    
    protected void emitCurrentWindowCounts() {
        Map<Object, Double> counts = counter.getCountsThenAdvanceWindow();
        int actualWindowLengthInSeconds = lastModifiedTracker.secondsSinceOldestModification();
        lastModifiedTracker.markAsModified();
        if (actualWindowLengthInSeconds != windowLengthInSeconds) {
            LOG.warn(String.format(WINDOW_LENGTH_WARNING_TEMPLATE, actualWindowLengthInSeconds, windowLengthInSeconds));
        }
        emit(counts);
    }
    
    protected void emit(Map<Object, Double> counts) {
        for (Entry<Object, Double> entry : counts.entrySet()) {
            Object hostname = entry.getKey();
            Double count = entry.getValue();
            collector.emit(new Values(hostname, count));
        }
        writeOutputToFile();
    }
    
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("hostname", "count"));
    }

    
    @Override
    public Map<String, Object> getComponentConfiguration() {
        Map<String, Object> conf = new HashMap<String, Object>();
        conf.put(Config.TOPOLOGY_TICK_TUPLE_FREQ_SECS, emitFrequencyInSeconds);
        return conf;
    }
    
    protected Logger getLogger() {
        return LOG;
    }
    
    private void writeOutputToFile(){
        PrintWriter writer = null;
        
        try {
            File file = new File("./analytics/Usage/" + topic +".txt");
            file.getParentFile().mkdirs();
            writer = new PrintWriter(file);
            //Sort the map before writing to a file
            ValueComparator bvc =  new ValueComparator(counter.getCounts());
            TreeMap<Object,Double> sorted_map = new TreeMap<Object,Double>(bvc);
            sorted_map.putAll(counter.getCounts());
            writer.println(sorted_map + "\n");
            
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if ( writer != null )
            {
                writer.close();
            }
        }
    }
    //Class used to sort the MAP.
    class ValueComparator implements Comparator<Object> {
        
        Map<Object, Double> base;
        public ValueComparator(Map<Object, Double> map) {
            this.base = map;
        }
        
        // Note: this comparator imposes orderings that are inconsistent with equals.
        public int compare(Object a, Object b) {
            if (base.get(a) >= base.get(b)) {
                return -1;
            } else {
                return 1;
            } // returning 0 would merge keys
        }
    }
}
