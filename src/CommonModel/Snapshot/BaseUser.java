package CommonModel.Snapshot;

import CommonModel.GameModel.Card.SingleCard.PermitCard.PermitCard;
import CommonModel.GameModel.City.City;
import CommonModel.GameModel.Path.Position;
import Server.Model.User;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Emanuele on 18/05/2016.
 */
public class BaseUser implements Serializable {

    protected String username;
    protected boolean connected;
    protected int coinPathPosition;
    protected int victoryPathPosition;
    protected Position nobilityPathPosition;
    protected ArrayList<City> usersEmporium;
    protected int helpers;
    protected ArrayList<PermitCard> permitCards;
    protected ArrayList<PermitCard> oldPermitCards;
    protected int politicCardNumber;

    // default constructor for inheritance. Obligatory! Do not cancel!
    public BaseUser() {
    }

    public BaseUser(User user){
        this.username = user.getUsername();
        this.connected = user.isConnected();
        this.coinPathPosition = user.getCoinPathPosition();
        this.victoryPathPosition = user.getVictoryPathPosition();
        this.nobilityPathPosition = user.getNobilityPathPosition();
        this.usersEmporium = user.getUsersEmporium();
        this.helpers = user.getHelpers();
        this.permitCards = user.getPermitCards();
        this.oldPermitCards = user.getOldPermitCards();
        this.politicCardNumber = user.getPoliticCardSize();
    }

    @Override
    public String toString() {
        return "BaseUser{" +
                "username='" + username + '\'' +
                ", connected=" + connected +
                ", coinPathPosition=" + coinPathPosition +
                ", victoryPathPosition=" + victoryPathPosition +
                ", nobilityPathPosition=" + nobilityPathPosition +
                ", usersEmporium=" + usersEmporium +
                ", helpers=" + helpers +
                ", permitCards=" + permitCards +
                ", oldPermitCards=" + oldPermitCards +
                ", politicCardNumber=" + politicCardNumber +
                '}';
    }

    public boolean isConnected() {
        return connected;
    }
    public int getHelpers() {
        return helpers;
    }
    public Position getNobilityPathPosition() {
        return nobilityPathPosition;
    }
    public ArrayList<PermitCard> getOldPermitCards() {
        return oldPermitCards;
    }
    public ArrayList<PermitCard> getPermitCards() {
        return permitCards;
    }
    public String getUsername() {
        return username;
    }
    public ArrayList<City> getUsersEmporium() {
        return usersEmporium;
    }
    public int getVictoryPathPosition() {
        return victoryPathPosition;
    }
    public int getCoinPathPosition() {
        return coinPathPosition;
    }
    public int getPoliticCardNumber() {
        return politicCardNumber;
    }
}
