package Server.NetworkInterface.Listeners;

import Server.Controller.GamesManager;
import Server.Model.User;
import Server.NetworkInterface.Communication.SocketCommunication;
import Utilities.Class.Constants;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Emanuele on 09/05/2016.
 */
public class SocketListener implements Runnable {

    private static SocketListener socketListener;
    private final ServerSocket serverSocket;
    private final GamesManager gamesManager;

    private SocketListener(GamesManager gamesManager) throws IOException {
        this.serverSocket = new ServerSocket(Constants.SOCKET_PORT);
        this.gamesManager = gamesManager;
    }

    public static SocketListener getInstance(GamesManager gamesManager) throws IOException {
        if (SocketListener.socketListener == null) {
            SocketListener.socketListener = new SocketListener(gamesManager);
        }
        return SocketListener.socketListener;
    }

    @Override
    public void run() {
        System.out.println("Socket Listener Started");
        Socket clientSocket;
        ExecutorService executorService = Executors.newCachedThreadPool();
        while (true) {
            try {
                clientSocket = this.serverSocket.accept();
                System.out.println("Socket accepted");
                SocketCommunication socketCommunication = new SocketCommunication(clientSocket);
                User user = new User(socketCommunication, this.gamesManager);
                socketCommunication.setUser(user);
                this.gamesManager.AddToUsers(user);
                executorService.execute(socketCommunication);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
