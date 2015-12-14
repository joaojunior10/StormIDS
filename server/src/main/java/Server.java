import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;

/**
 * Created by joao on 10/12/15.
 */
public class Server {
    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = null;
        int packets = Integer.parseInt(args[0]);
        boolean listening = true;
        try {
            serverSocket = new ServerSocket(3000);
        } catch (IOException e) {
            System.err.println("Nao foi possivel ouvir a porta 3000.");
            System.exit(-1);
        }

        while (listening) {
            try {
                new Worker(serverSocket.accept(),
                        packets).start();
            } catch (SocketException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try {
            serverSocket.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
