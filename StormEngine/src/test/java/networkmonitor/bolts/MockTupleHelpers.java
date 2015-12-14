package networkmonitor.bolts;

import backtype.storm.Constants;
import backtype.storm.tuple.Tuple;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by joao on 11/12/15.
 */
public final class MockTupleHelpers {

    private MockTupleHelpers() {
    }

    public static Tuple mockTickTuple(String matches) {
        return mockTuple(Constants.SYSTEM_COMPONENT_ID, Constants.SYSTEM_TICK_STREAM_ID, matches);
    }

    public static Tuple mockTuple(String componentId, String streamId,String matches) {
        Tuple tuple = mock(Tuple.class);
        when(tuple.getSourceComponent()).thenReturn(componentId);
        when(tuple.getSourceStreamId()).thenReturn(streamId);
        when(tuple.getValue(0)).thenReturn(matches);
        return tuple;
    }
}
