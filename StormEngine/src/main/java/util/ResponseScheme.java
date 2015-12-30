package util;
import backtype.storm.spout.Scheme;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import util.Response;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.util.List;

/**
 * Created by jjunior on 30/12/2015.
 */
public class ResponseScheme implements Scheme {
    @Override
    public List<Object> deserialize(byte[] ser) {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(ser);
            ObjectInput in = new ObjectInputStream(bis);
            Response response = (Response) in.readObject();
            return new Values(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Fields getOutputFields() {
        return new Fields("response");
    }
}
