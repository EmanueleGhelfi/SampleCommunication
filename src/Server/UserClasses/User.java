package Server.UserClasses;

import CommonModel.GameImmutable;
import Server.Communication.BaseCommunication;
import Server.Model.Game;
import Server.Managers.GamesManager;

/**
 * Created by Emanuele on 11/05/2016.
 */
public class User {

    private BaseCommunication baseCommunication;

    private String username;

    private GamesManager gamesManager;

    private GameImmutable game;

    private boolean connected;

    public User(BaseCommunication baseCommunication, GamesManager gamesManager) {
        this.baseCommunication = baseCommunication;
        this.username = "DummyId";
        this.gamesManager = gamesManager;
        this.game = null;
        this.connected=true;
    }

    /**
     * Called when the user receive a message on RMI or socket
     * @param message the message
     */
    public void OnMessage(String message){
        game.OnMessage(message);
    }

    /**
     *
     * @return the BaseCommunication (RMICommunication or SocketCommunication)
     */
    public BaseCommunication getBaseCommunication() {
        return baseCommunication;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void notifyGameStart() {
        baseCommunication.sendMessage("PARTITA INIZIATA");
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
