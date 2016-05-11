package Server.UserClasses;

import Server.Communication.BaseCommunication;
import Server.Managers.Game;
import Server.Managers.GameManager;

/**
 * Created by Emanuele on 11/05/2016.
 */
public class User {

    private BaseCommunication baseCommunication;

    private String username;

    private GameManager gameManager;

    private Game game;

    public User(BaseCommunication baseCommunication, GameManager gameManager) {
        this.baseCommunication = baseCommunication;
        this.username = "DummyId";
        this.gameManager = gameManager;
        this.game = null;
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

    public void setGame(Game game) {
        this.game = game;
    }
}
