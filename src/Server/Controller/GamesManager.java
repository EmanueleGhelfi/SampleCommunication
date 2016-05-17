package Server.Controller;

import Interface.RMIListenerInterface;
import Server.NetworkInterface.Listeners.RMIListener;
import Server.NetworkInterface.Listeners.SocketListener;
import Server.Model.Game;
import Server.Model.User;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.util.ArrayList;

/**
 * Created by Emanuele on 09/05/2016.
 */
public class GamesManager {

    private ArrayList<User> users = new ArrayList<>();

    private static GamesManager gamesManager;

    /**
     * Created games (and maybe started)
     */
    private ArrayList<Game> games = new ArrayList<>();

    private GamesManager(){
        start();
    }

    public static GamesManager getInstance(){
        if(gamesManager ==null){
            gamesManager = new GamesManager();
        }

        return gamesManager;

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
                System.out.println("adding user to a game");
                game.addUserToGame(userToAdd);
                userToAdd.setGame(game);
                return;
            }
        }

        System.out.println("creating a new game");
        Game game = new Game();
        games.add(game);
        System.out.println(game + " GAMEEEEEEEEEEEEEEEEEEEEEEEEEE PORCA MADDONA");
        game.addUserToGame(userToAdd);
        userToAdd.setGame(game);
        System.out.println(userToAdd + " USERRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR PORCA MADDONA");

    }


    public boolean userAlreadyPresent(String username){
        System.out.println("User already present called");
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
