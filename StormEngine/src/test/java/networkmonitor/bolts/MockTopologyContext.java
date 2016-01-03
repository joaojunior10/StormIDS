package networkmonitor.bolts;

import backtype.storm.task.TopologyContext;
import backtype.storm.tuple.Tuple;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by joao on 2/1/16.
 */
public class MockTopologyContext {
    public static TopologyContext mockTopologyContext() {
        TopologyContext context = mock(TopologyContext.class);
        when(context.getThisTaskId()).thenReturn(1);
        return context;
    }
}
