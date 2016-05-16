package CommonModel.GameModel.Action;

import CommonModel.GameModel.Card.PoliticCard;
import CommonModel.GameModel.City.Region;
import Server.Model.Game;
import Server.UserClasses.User;

import java.util.ArrayList;

/**
 * Created by Emanuele on 16/05/2016.
 */
public class BuyPermitCard implements Action {

    private final String type = "MAIN_ACTION";
    private Region userRegion;
    private ArrayList<PoliticCard> politicCard;

    public BuyPermitCard(ArrayList<PoliticCard> politicCard, Region userRegion) {
        this.politicCard = politicCard;
        this.userRegion = userRegion;
    }

    @Override
    public void doAction(Game game, User user) {
        Region region = game.getRegion(userRegion.getRegion());


    }

    @Override
    public String getType() {
        return null;
    }
}
