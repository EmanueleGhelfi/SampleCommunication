package CommonModel.Snapshot;

import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticCard;
import Server.Model.User;

import java.util.ArrayList;

/**
 * Created by Emanuele on 18/05/2016.
 */
public class CurrentUser extends BaseUser {

    protected ArrayList<PoliticCard> politicCards;

    protected int mainActionCounter=0;

    protected int fastActionCounter = 0;


    public CurrentUser() {
    }

    public ArrayList<PoliticCard> getPoliticCards() {
        return politicCards;
    }
}
