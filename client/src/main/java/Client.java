import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by joao on 10/12/15.
 */
public class Client {
    public static void main(String[] args) throws IOException {

        String ipAddress = args[0];
        int packet = Integer.parseInt(args[1]);
        int tasks = Integer.parseInt(args[2]);

        for (int i = 0; i < tasks; i++) {
            try {
                new Worker(new Socket(ipAddress, 3000),packet,ipAddress,i).start();
            } catch (SocketException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
