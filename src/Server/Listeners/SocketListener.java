package Server.Listeners;

import Server.Communication.SocketCommunication;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Emanuele on 09/05/2016.
 */
public class SocketListener implements Runnable {

    private ServerSocket serverSocket;
    private static SocketListener socketListener;

    private SocketListener( ) throws IOException {
        serverSocket = new ServerSocket(4333);

    }

    public static SocketListener getInstance() throws IOException {
        if(socketListener==null){
            socketListener = new SocketListener();
        }

        return socketListener;
    }

    @Override
    public void run() {

        System.out.println("Socket Listener Started");
        Socket clientSocket;
        ExecutorService executorService = Executors.newCachedThreadPool();

        while (true){
            try {
                clientSocket = serverSocket.accept();
                System.out.println("Socket accepted");
                SocketCommunication socketCommunication = new SocketCommunication(clientSocket);
                executorService.execute(socketCommunication);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
