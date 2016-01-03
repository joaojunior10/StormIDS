package networkmonitor.bolts;

import backtype.storm.task.OutputCollector;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by joao on 2/1/16.
 */
public class MockOutputCollector {
    public static OutputCollector mockOutputCollector() {
        OutputCollector collector = mock(OutputCollector.class);
        when(collector.emit(null)).thenReturn(null);

        return collector;
    }
}
