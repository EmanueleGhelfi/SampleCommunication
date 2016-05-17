package Server.UserClasses;

import CommonModel.GameModel.City.City;
import CommonModel.GameModel.Position;
import Server.Communication.BaseCommunication;
import Server.Model.Game;
import Server.Managers.GamesManager;
import com.sun.deploy.ref.Helpers;

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

    private int helpers;

    private int mainActionCounter=0;

    private int fastActionCounter = 0;

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

    public int getHelpers() {
        return helpers;
    }

    public void setHelpers(int helpers) {
        this.helpers = helpers;
    }

    public int getFastActionCounter() {
        return fastActionCounter;
    }

    public void setFastActionCounter(int fastActionCounter) {
        this.fastActionCounter = fastActionCounter;
    }

    public int getMainActionCounter() {
        return mainActionCounter;
    }

    public void setMainActionCounter(int mainActionCounter) {
        this.mainActionCounter = mainActionCounter;
    }

    @Override
    public String toString() {
        return "User{" +
                "baseCommunication=" + baseCommunication +
                ", username='" + username + '\'' +
                ", gamesManager=" + gamesManager +
                ", game=" + game +
                ", connected=" + connected +
                ", coinPathPosition=" + coinPathPosition +
                ", victoryPathPosition=" + victoryPathPosition +
                ", nobilityPathPosition=" + nobilityPathPosition +
                ", usersEmporium=" + usersEmporium +
                ", helpers=" + helpers +
                '}';
    }
}
