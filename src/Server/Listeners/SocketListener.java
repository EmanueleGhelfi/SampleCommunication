package Server.Listeners;

import Server.Communication.SocketCommunication;
import Server.Managers.GameManager;
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
    private GameManager gameManager;

    private SocketListener(GameManager gameManager) throws IOException {
        serverSocket = new ServerSocket(4333);
        this.gameManager = gameManager;
    }

    public static SocketListener getInstance(GameManager gameManager) throws IOException {
        if(socketListener==null){
            socketListener = new SocketListener(gameManager);
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
                User user = new User(socketCommunication, gameManager);
                socketCommunication.setUser(user);
                gameManager.AddToUsers(user);
                executorService.execute(socketCommunication);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
