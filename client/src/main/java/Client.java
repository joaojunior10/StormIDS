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
        int packets = Integer.parseInt(args[1]);
        int count = 0;
        try {
            socket = new Socket(ipAddress, 3000);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            System.out.println("Transmissão iniciado em " + sdf.format(System.currentTimeMillis()));

            for (int i = 0; i < packets; i++){
                count++;
                byte[] bytes = new byte[256];
                in.read(bytes);
            }
            System.out.println("Pacotes lidos "+count);
            System.out.println("Transmissão terminada em " + sdf.format(System.currentTimeMillis()));
            socket.close();
        } catch (Exception e) {
            System.out.println("Pacotes lidos "+count);
            System.out.println("Fim da transmição");
        }

    }
}
