package CommonModel.GameModel.Action;

import CommonModel.GameModel.Card.PermitCard;
import CommonModel.GameModel.Card.PoliticCard;
import CommonModel.GameModel.City.Region;
import Server.Model.Game;
import Server.UserClasses.User;

import java.util.ArrayList;

/**
 * Created by Emanuele on 16/05/2016.
 */
public class BuyPermitCard extends Action {


    private Region userRegion;
    private ArrayList<PoliticCard> politicCard;
    private PermitCard permitCard;


    public BuyPermitCard(ArrayList<PoliticCard> politicCard, Region userRegion, PermitCard permitCard) {
        this.politicCard = politicCard;
        this.userRegion = userRegion;
        this.type ="MAIN_ACTION";
        this.permitCard = permitCard;
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
