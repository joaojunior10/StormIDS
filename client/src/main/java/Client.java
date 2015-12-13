import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by joao on 10/12/15.
 */
public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = null;
        String ipAddress = args[0];
        int count = 0;
        try {
            socket = new Socket(ipAddress, 3000);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            System.out.println("Transmissão iniciado em " + sdf.format(System.currentTimeMillis()));
            System.out.println("Millis " + System.currentTimeMillis());


            while (true){
                count++;
                byte[] bytes = new byte[256];
                in.read(bytes);
                out.writeByte(1);
                out.flush();
            }
        } catch (Exception e) {
            System.out.println("Pacotes lidos "+count);
            System.out.println("Fim da transmição");
        }

    }
}
