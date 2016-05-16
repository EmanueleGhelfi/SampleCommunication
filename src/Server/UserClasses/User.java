package Server.UserClasses;

import CommonModel.GameModel.City.City;
import CommonModel.GameModel.Position;
import Server.Communication.BaseCommunication;
import Server.Model.Game;
import Server.Managers.GamesManager;

import java.util.ArrayList;

/**
 * Created by Emanuele on 11/05/2016.
 */
public class User {

    private BaseCommunication baseCommunication;

    private String username;

    private GamesManager gamesManager;

    private Game game;

    private boolean connected;

    private int coinPathPosition;

    private int victoryPathPosition;

    private Position nobilityPathPosition;

    private ArrayList<City> usersEmporium;

    public User(BaseCommunication baseCommunication, GamesManager gamesManager) {
        this.baseCommunication = baseCommunication;
        this.username = "DummyId";
        this.gamesManager = gamesManager;
        this.game = null;
        this.connected=true;
        usersEmporium = new ArrayList<>();
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
        baseCommunication.notifyGameStart();
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

    public void setCoinPathPosition(int coinPathPosition) {
        this.coinPathPosition = coinPathPosition;
    }

    public int getCoinPathPosition() {
        return coinPathPosition;
    }

    public int getVictoryPathPosition() {
        return victoryPathPosition;
    }

    public void setVictoryPathPosition(int victoryPathPosition) {
        this.victoryPathPosition = victoryPathPosition;
    }

    public Position getNobilityPathPosition() {
        return nobilityPathPosition;
    }

    public void setNobilityPathPosition(Position nobilityPathPosition) {
        this.nobilityPathPosition = nobilityPathPosition;
    }

    public Game getGame() {
        return game;
    }

    public ArrayList<City> getUsersEmporium() {
        return usersEmporium;
    }

    public void addEmporium(City cityEmporium) {
        this.usersEmporium.add(cityEmporium);
    }
}
