package CommonModel.Snapshot;

import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticCard;
import Server.Model.User;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Emanuele on 18/05/2016.
 */
public class CurrentUser extends BaseUser {

    protected ArrayList<PoliticCard> politicCards;
    protected int mainActionCounter;
    protected int fastActionCounter;
    protected int optionalActionCounter;

    public CurrentUser() {
    }

    public CurrentUser(User user) {
        super(user);
        politicCards = user.getPoliticCards();
        mainActionCounter = user.getMainActionCounter();
        fastActionCounter = user.getFastActionCounter();
    }

    @Override
    public String toString() {
        return "CurrentUser{" +
                "politicCards=" + this.politicCards +
                ", mainActionCounter=" + this.mainActionCounter +
                ", fastActionCounter=" + this.fastActionCounter +
                '}';
    }

    public ArrayList<PoliticCard> getPoliticCards() {
        return this.politicCards;
    }

    public int getMainActionCounter() {
        return this.mainActionCounter;
    }

    public int getFastActionCounter() {
        return this.fastActionCounter;
    }

    public int getOptionalActionCounter() {
        return this.optionalActionCounter;
    }
}
