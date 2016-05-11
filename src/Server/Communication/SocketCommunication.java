package Server.Communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Emanuele on 09/05/2016.
 */
public class SocketCommunication extends BaseCommunication implements Runnable {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public SocketCommunication(Socket socket) throws IOException {
        this.socket = socket;
        //Open the buffers
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }


    @Override
    public void sendMessage(String message) {
            out.println(message);
    }

    @Override
    public void run() {
        String line;
        System.out.println("Socket communication started");
        try {
            while ((line = in.readLine()) != null) {
                System.out.println("Received in Socket : "+line);
                out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
