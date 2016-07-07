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

/** This class has the aim of managing the various Games.
 * For here we find methods such as the addition of the game, its cancellation and verifying whether a user is down there or not.
 * Created by Emanuele on 09/05/2016.
 */
public class GamesManager {

    private static GamesManager gamesManager;
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<Game> games = new ArrayList<>();

    /** Constructor private (for singleton pattern)
     */
    private GamesManager() {
        start();
    }

    /** Used for the creation of a new Games Manager according to the Singleton pattern
     * @return the games manager
     */
    public static GamesManager getInstance() {
        if (gamesManager == null) {
            gamesManager = new GamesManager();
        }
        return gamesManager;
    }

    /** Register the communication and start the threads
     */
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

    /** Add a user to the game
     * @param userToAdd is the user added
     */
    public void addToGame(User userToAdd) {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        for (Game game : games) {
            if (!game.isStarted()) {
                game.addUserToGame(userToAdd);
                userToAdd.setGame(game);
                userToAdd.setGameController(game.getGameController());
                return;
            }
        }
        Game game = new Game();
        games.add(game);
        System.out.println(game);
        game.addUserToGame(userToAdd);
        userToAdd.setGame(game);
        userToAdd.setGameController(game.getGameController());
        System.out.println(userToAdd);
    }

    /** Specify that a user with this name is already present
     * @param username is the username to check
     * @return true if is already present
     */
    public boolean userAlreadyPresent(String username) {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    /** The user is added to other users in game
     * @param user is the user to add
     */
    public void AddToUsers(User user) {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        users.add(user);
    }

    /** The game is cancelled
     * @param game is the game that must be cancelled
     * @param gameController is the gameController of the game
     */
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
