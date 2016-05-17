package Server.NetworkInterface.Listeners;

import Server.NetworkInterface.Communication.SocketCommunication;
import Server.Managers.GamesManager;
import Server.Model.User;

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
    private GamesManager gamesManager;

    private SocketListener(GamesManager gamesManager) throws IOException {
        serverSocket = new ServerSocket(4333);
        this.gamesManager = gamesManager;
    }

    public static SocketListener getInstance(GamesManager gamesManager) throws IOException {
        if(socketListener==null){
            socketListener = new SocketListener(gamesManager);
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
                User user = new User(socketCommunication, gamesManager);
                socketCommunication.setUser(user);
                gamesManager.AddToUsers(user);
                executorService.execute(socketCommunication);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
