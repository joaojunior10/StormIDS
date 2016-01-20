import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Random;

public class Worker extends Thread {
    Socket socket = null;
    ObjectOutputStream out = null;
    ObjectInputStream in = null;
    int count = 0;

    public Worker(Socket socket, int count) {
        super();
        this.socket = socket;
        this.count = count;
    }

    @Override
    public void run() {

        try {
            System.out.println("Conexao criada com:"
                    + socket.getInetAddress().toString());
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            System.out.println("Transmissão iniciada em " + sdf.format(System.currentTimeMillis()));

            for (int i = 0; i< count; i++) {
                Random random = new Random();
                byte[] buffer = new byte[658];
                random.nextBytes(buffer);
                byte[] msg = ("StormIDS Test" + Integer.toString(i)).getBytes();
                for (int j = 0; j < msg.length; j++){
                    buffer[j] =  msg[j];
                }

                out.write(buffer);
                out.flush();
                in.readByte();
            }
            System.out.println("Enviados " + count+ " pacotes");
            System.out.println("Transmissão terminada em " + sdf.format(System.currentTimeMillis()));
//            socket.close();
        } catch (Exception e) {
            System.err.println("Cliente terminou processamento ");
        }
    }
}