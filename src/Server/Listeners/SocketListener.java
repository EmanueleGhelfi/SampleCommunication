package Server.Listeners;

import Server.Communication.SocketCommunication;
import Server.Main.Server;
import Server.UserClasses.User;

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
    private Server server;

    private SocketListener(Server server) throws IOException {
        serverSocket = new ServerSocket(4333);
        this.server = server;
    }

    public static SocketListener getInstance(Server server) throws IOException {
        if(socketListener==null){
            socketListener = new SocketListener(server);
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
                User user = new User(socketCommunication, "provetta");
                server.AddToUsers(user);
                executorService.execute(socketCommunication);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
