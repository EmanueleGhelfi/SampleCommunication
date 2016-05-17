package CommonModel.GameModel.Action;

import Utilities.Exception.ActionNotPossibleException;
import CommonModel.GameModel.Card.Deck.PermitDeck;
import CommonModel.GameModel.Card.PermitCard;
import CommonModel.GameModel.Card.PoliticCard;
import CommonModel.GameModel.City.Region;
import Server.Model.Game;
import Server.Model.User;

import java.util.ArrayList;

/**
 * Created by Emanuele on 16/05/2016.
 */
public class BuyPermitCard extends Action {


    private Region userRegion;
    private ArrayList<PoliticCard> politicCard;
    private PermitCard permitCard;


    // TODO: test
    public BuyPermitCard(ArrayList<PoliticCard> politicCard, Region userRegion, PermitCard permitCard) {
        this.politicCard = politicCard;
        this.userRegion = userRegion;
        this.type ="MAIN_ACTION";
        this.permitCard = permitCard;
    }

    @Override
    public void doAction(Game game, User user) throws ActionNotPossibleException {
        Region region = game.getRegion(userRegion.getRegion());
        PermitDeck permitDeck = game.getPermitDeck(region);
        PermitCard permitCardToBuy = permitDeck.getPermitCardVisible(permitCard);
        permitCardToBuy.getBonus().getBonus(user,game);
        user.addPermitCard(permitCardToBuy);
        int newPositionInMoneyPath =0;
        if(politicCard.size()<4 && politicCard.size()>0)
            newPositionInMoneyPath=10-(politicCard.size()-1);
        else if(politicCard.size()==4)
            newPositionInMoneyPath = 0;
            else throw new ActionNotPossibleException();
        game.getMoneyPath().goAhead(user,newPositionInMoneyPath);
        //TODO: multicolor card
        removeAction(game, user);
    }

}
