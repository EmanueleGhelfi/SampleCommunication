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

/** This class acts as a listener for the Socket method.
 * Created by Emanuele on 09/05/2016.
 */
public class SocketListener implements Runnable {

    private static SocketListener socketListener;
    private ServerSocket serverSocket;
    private GamesManager gamesManager;

    private SocketListener(GamesManager gamesManager) throws IOException {
        serverSocket = new ServerSocket(Constants.SOCKET_PORT);
        this.gamesManager = gamesManager;
    }

    public static SocketListener getInstance(GamesManager gamesManager) throws IOException {
        if (socketListener == null) {
            socketListener = new SocketListener(gamesManager);
        }
        return socketListener;
    }

    @Override
    public void run() {
        Socket clientSocket;
        ExecutorService executorService = Executors.newCachedThreadPool();
        while (true) {
            try {
                clientSocket = serverSocket.accept();
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
