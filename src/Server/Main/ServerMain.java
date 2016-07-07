package Server.Main;

import Server.Controller.GamesManager;

/** This class has the main method and it's used to start the server creating a gamesManager.
 * Created by Emanuele on 09/05/2016.
 */
public class ServerMain {

    public static void main(String[] args) {
        GamesManager gamesManager = GamesManager.getInstance();
    }

}
