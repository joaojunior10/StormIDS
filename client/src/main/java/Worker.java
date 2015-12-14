import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Random;

/**
 * Created by joao on 14/12/15.
 */
public class Worker extends Thread{
    Socket socket = null;
    String ipAddress;
    ObjectOutputStream out = null;
    ObjectInputStream in = null;
    int packets;
    int clientId;

    public Worker(Socket socket, int packets, String ipAddress, int clientId) {
        super();
        this.socket = socket;
        this.packets = packets;
        this.ipAddress = ipAddress;
        this.clientId = clientId;
    }

    @Override
    public void run() {
        int count = 0;
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            System.out.println(this.clientId+" - Transmissão iniciado em " + sdf.format(System.currentTimeMillis()));

            for (int i = 0; i < packets; i++){
                count++;
                byte[] bytes = new byte[256];
                in.read(bytes);
                out.writeByte(1);
                out.flush();
            }
            System.out.println(this.clientId+" - Pacotes lidos " + count);
            System.out.println(this.clientId+" - Transmissão terminada em " + sdf.format(System.currentTimeMillis()));
            socket.close();
        } catch (Exception e) {
            System.out.println(this.clientId+" - Pacotes lidos "+count);
            System.out.println(this.clientId+" - Fim da transmição");
        }
    }
}
