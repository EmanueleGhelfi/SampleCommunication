package Server.Managers;

import Interface.RMIListenerInterface;
import Server.Listeners.RMIListener;
import Server.Listeners.SocketListener;
import Server.UserClasses.User;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.util.ArrayList;

/**
 * Created by Emanuele on 09/05/2016.
 */
public class GameManager {

    private ArrayList<User> users = new ArrayList<>();

    private static GameManager gameManager;

    /**
     * Created games (and maybe started)
     */
    private ArrayList<Game> games = new ArrayList<>();

    private GameManager(){
        start();
    }

    public static GameManager getInstance(){
        if(gameManager==null){
            gameManager = new GameManager();
        }

        return gameManager;

    }

    public void start(){
        try {
            RMIListenerInterface rmiListener = new RMIListener(this);
            Registry registry=null;
        try{
             registry = LocateRegistry.createRegistry(1099);
        }
        catch (ExportException e){
            e.printStackTrace();
            registry = LocateRegistry.getRegistry();

        }
        registry.rebind("server",rmiListener);

            SocketListener socketListener = SocketListener.getInstance(this);
            Thread thread = new Thread(socketListener);
            thread.start();


        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addToGame(User userToAdd){
        for (Game game: games) {
            if(!game.isStarted()){
                game.addUserToGame(userToAdd);
                userToAdd.setGame(game);
            }
        }
    }

    /**
     * Send message to all client
     * @param message
     */
    public void OnMessage(String message) {

        System.out.println("On message GameManager");
        for (User user: users) {
            user.getBaseCommunication().sendMessage(message);
        }
    }

    public boolean userAlreadyPresent(String username){
        for (User user: users) {
            if(user.getUsername().equals(username)){
                return true;
            }

        }

        return false;
    }

    public void AddToUsers(User user) {
        users.add(user);
    }
}
