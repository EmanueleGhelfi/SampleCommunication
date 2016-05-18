package CommonModel.Snapshot;

import CommonModel.GameModel.Card.SingleCard.PermitCard.PermitCard;
import CommonModel.GameModel.City.City;
import CommonModel.GameModel.Path.Position;
import Server.Model.User;

import java.util.ArrayList;

/**
 * Created by Emanuele on 18/05/2016.
 */
public class BaseUser {

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


    public int getCoinPathPoistion() {
        return coinPathPosition;
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

    public int getPoliticCardNumber() {
        return politicCardNumber;
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



}
