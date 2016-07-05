package Server.Controller;

import RMIInterface.RMIListenerInterface;
import Server.Model.Game;
import Server.Model.User;
import Server.NetworkInterface.Listeners.RMIListener;
import Server.NetworkInterface.Listeners.SocketListener;
import Utilities.Class.Constants;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Created by Emanuele on 09/05/2016.
 */
public class GamesManager {

    private static GamesManager gamesManager;
    private final ArrayList<User> users = new ArrayList<>();
    /**
     * Created games (and maybe started)
     */
    private final ArrayList<Game> games = new ArrayList<>();

    private GamesManager() {
        this.start();
    }

    public static GamesManager getInstance() {
        if (GamesManager.gamesManager == null) {
            GamesManager.gamesManager = new GamesManager();
        }
        return GamesManager.gamesManager;
    }

    public void start() {
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
        for (Game game : this.games) {
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
        this.games.add(game);
        System.out.println(game);
        game.addUserToGame(userToAdd);
        userToAdd.setGame(game);
        userToAdd.setGameController(game.getGameController());
        System.out.println(userToAdd);
    }


    public boolean userAlreadyPresent(String username) {
        System.out.println("User already present called");
        for (User user : this.users) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public void AddToUsers(User user) {
        this.users.add(user);
    }

    public void cancelThisGame(Game game, GameController gameController) {
        for (Entry<String, User> userInGame : game.getUsersInGame().entrySet()) {
            this.users.remove(userInGame.getValue());
        }
        for (int i = 0; i < this.games.size(); i++) {
            if (this.games.get(i).equals(game))
                this.games.remove(i);
        }
    }
}
