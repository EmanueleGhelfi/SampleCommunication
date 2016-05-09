package Client.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Emanuele on 09/05/2016.
 */
public class ClientSocketService extends ClientService implements Runnable {

    private String hostname = "localhost";
    private int port = 4333;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;



    @Override
    public void SendMessage(String message) {
        System.out.println("sended "+message);
        out.println(message);
    }

    @Override
    public boolean Connect() {
        try {
            socket = new Socket(hostname,port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out.println("HELLO");
            Thread thread = new Thread(this);
            thread.start();
            System.out.println("Thread started");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;

    }

     ClientSocketService(){

    }


    @Override
    public void run() {
        System.out.println("ClientSocketService Started");
        String line = null;
        try {
            while ( (line = in.readLine())!=null){

            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
