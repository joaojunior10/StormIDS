package util;

import kafka.serializer.Encoder;
import kafka.utils.VerifiableProperties;
import util.Response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

/**
 * Created by jjunior on 23/12/2015.
 */
public class ResponseEncoder implements Encoder<Response> {
    public ResponseEncoder(VerifiableProperties verifiableProperties) {
        int test = 1;
        /* This constructor must be present for successful compile. */
    }
    @Override
    public byte[] toBytes(Response response) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(bos);
            out.writeObject(response);
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
