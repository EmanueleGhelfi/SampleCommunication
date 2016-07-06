package Server.Controller;

import RMIInterface.RMIListenerInterface;
import Server.Model.Game;
import Server.Model.User;
import Server.NetworkInterface.Listeners.RMIListener;
import Server.NetworkInterface.Listeners.SocketListener;
import Utilities.Class.Constants;
import Utilities.Class.InternalLog;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Emanuele on 09/05/2016.
 */
public class GamesManager {

    private static GamesManager gamesManager;
    private ArrayList<User> users = new ArrayList<>();
    /**
     * Created games (and maybe started)
     */
    private ArrayList<Game> games = new ArrayList<>();

    private GamesManager() {
        start();
    }

    public static GamesManager getInstance() {
        if (gamesManager == null) {
            gamesManager = new GamesManager();
        }
        return gamesManager;
    }

    public void start() {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        try {
            RMIListenerInterface rmiListener = new RMIListener(this);
            Registry registry = null;
            try {
                registry = LocateRegistry.createRegistry(Constants.RMI_PORT);
            } catch (ExportException e) {
                registry = LocateRegistry.getRegistry();
            }
            registry.rebind(Constants.SERVER, rmiListener);
            SocketListener socketListener = SocketListener.getInstance(this);
            Thread thread = new Thread(socketListener);
            thread.start();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addToGame(User userToAdd) {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        for (Game game : games) {
            if (!game.isStarted()) {
                System.out.println("adding user to a game");
                game.addUserToGame(userToAdd);
                userToAdd.setGame(game);
                userToAdd.setGameController(game.getGameController());
                return;
            }
        }
        System.out.println("creating a new game");
        Game game = new Game();
        games.add(game);
        System.out.println(game);
        game.addUserToGame(userToAdd);
        userToAdd.setGame(game);
        userToAdd.setGameController(game.getGameController());
        System.out.println(userToAdd);
    }


    public boolean userAlreadyPresent(String username) {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public void AddToUsers(User user) {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        users.add(user);
    }

    public void cancelThisGame(Game game, GameController gameController) {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        for (Map.Entry<String, User> userInGame : game.getUsersInGame().entrySet()) {
            users.remove(userInGame.getValue());
        }
        for (int i = 0; i < games.size(); i++) {
            if (games.get(i).equals(game))
                games.remove(i);
        }
    }
}
