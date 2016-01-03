package networkmonitor.bolts;

import backtype.storm.Constants;
import backtype.storm.tuple.Tuple;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by joao on 11/12/15.
 */
public final class MockTuple {

    private MockTuple() {
    }

    public static Tuple mockTickTuple(Object toMock) {
        return mockTuple(Constants.SYSTEM_COMPONENT_ID, Constants.SYSTEM_TICK_STREAM_ID, toMock);
    }

    public static Tuple mockTuple(String componentId, String streamId,Object toMock) {
        Tuple tuple = mock(Tuple.class);
        when(tuple.getSourceComponent()).thenReturn(componentId);
        when(tuple.getSourceStreamId()).thenReturn(streamId);
        when(tuple.getValue(0)).thenReturn(toMock);
        return tuple;
    }
}
