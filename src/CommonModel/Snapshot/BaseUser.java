package CommonModel.Snapshot;

import CommonModel.GameModel.Card.SingleCard.PermitCard.PermitCard;
import CommonModel.GameModel.City.City;
import CommonModel.GameModel.Council.Helper;
import CommonModel.GameModel.Path.Position;
import Server.Model.FakeUser;
import Server.Model.User;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Emanuele on 18/05/2016.
 */
public class BaseUser implements Serializable {

    protected String username;
    protected UserColor userColor;
    protected boolean connected;
    protected int coinPathPosition;
    protected int victoryPathPosition;
    protected Position nobilityPathPosition;
    protected ArrayList<City> usersEmporium;
    protected ArrayList<Helper> helpers = new ArrayList<>();
    protected ArrayList<PermitCard> permitCards;
    protected ArrayList<PermitCard> oldPermitCards;
    protected int politicCardNumber;
    protected boolean fakeUser;

    // default constructor for inheritance. Obligatory! Do not cancel!
    public BaseUser() {
    }

    public BaseUser(User user) {
        username = user.getUsername();
        userColor = user.getUserColor();
        connected = user.isConnected();
        coinPathPosition = user.getCoinPathPosition();
        victoryPathPosition = user.getVictoryPathPosition();
        nobilityPathPosition = user.getNobilityPathPosition();
        usersEmporium = user.getUsersEmporium();
        helpers = user.getHelpers();
        permitCards = user.getPermitCards();
        oldPermitCards = user.getOldPermitCards();
        politicCardNumber = user.getPoliticCardSize();
        if (user instanceof FakeUser) {
            this.fakeUser = true;
        }
    }

    @Override
    public String toString() {
        return "BaseUser{" +
                "username='" + this.username + '\'' +
                ", connected=" + this.connected +
                ", coinPathPosition=" + this.coinPathPosition +
                ", victoryPathPosition=" + this.victoryPathPosition +
                ", nobilityPathPosition=" + this.nobilityPathPosition +
                ", usersEmporium=" + this.usersEmporium +
                ", helpers=" + this.helpers.size() +
                ", permitCards=" + this.permitCards +
                ", oldPermitCards=" + this.oldPermitCards +
                ", politicCardNumber=" + this.politicCardNumber +
                '}';
    }

    public boolean isConnected() {
        return this.connected;
    }

    public ArrayList<Helper> getHelpers() {
        return this.helpers;
    }

    public Position getNobilityPathPosition() {
        return this.nobilityPathPosition;
    }

    public ArrayList<PermitCard> getOldPermitCards() {
        return this.oldPermitCards;
    }

    public ArrayList<PermitCard> getPermitCards() {
        return this.permitCards;
    }

    public String getUsername() {
        return this.username;
    }

    public UserColor getUserColor() {
        return this.userColor;
    }

    public void setUserColor(UserColor userColor) {
        this.userColor = userColor;
    }

    public ArrayList<City> getUsersEmporium() {
        return this.usersEmporium;
    }

    public int getVictoryPathPosition() {
        return this.victoryPathPosition;
    }

    public int getCoinPathPosition() {
        return this.coinPathPosition;
    }

    public int getPoliticCardNumber() {
        return this.politicCardNumber;
    }

    public boolean isFakeUser() {
        return this.fakeUser;
    }
}
