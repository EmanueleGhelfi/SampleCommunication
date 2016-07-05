package CommonModel.GameModel.Action;

import CommonModel.GameModel.Card.Deck.PermitDeck;
import CommonModel.GameModel.Card.SingleCard.PermitCard.PermitCard;
import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticCard;
import CommonModel.GameModel.City.Region;
import CommonModel.GameModel.City.RegionName;
import Server.Model.Game;
import Server.Model.User;
import Utilities.Class.Constants;
import Utilities.Exception.ActionNotPossibleException;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Emanuele on 16/05/2016.
 */
public class MainActionBuyPermitCard extends Action {

    private final RegionName userRegion;
    private final ArrayList<PoliticCard> politicCards;
    private final PermitCard permitCard;
    private int bonusCounter;

    public MainActionBuyPermitCard(ArrayList<PoliticCard> politicCard, RegionName userRegion, PermitCard permitCard) {
        politicCards = politicCard;
        this.userRegion = userRegion;
        actionType = Constants.MAIN_ACTION;
        this.permitCard = permitCard;
    }

    @Override
    public void doAction(Game game, User user) throws ActionNotPossibleException {
        if (checkActionCounter(user) && this.politicCards.size() > 0) {
            // count number of correct politic card
            int correctPoliticCard = 0;
            // region of permit card
            Region region = game.getRegion(this.userRegion);
            //this is the new position of the user in money path
            int newPositionInMoneyPath = 0;
            // calculate correct politic card
            correctPoliticCard = this.countCorrectPoliticCard(region, this.politicCards, this.bonusCounter);
            // calculate money to spend
            newPositionInMoneyPath = this.calculateMoney(correctPoliticCard, this.politicCards, this.bonusCounter);
            // go ahead in money path
            game.getMoneyPath().goAhead(user, -newPositionInMoneyPath);
            // re-add to game deck
            game.getPoliticCards().addToQueue(new HashSet<PoliticCard>(this.politicCards));
            // remove cards from user
            int cont2 = 0;
            this.removePoliticCard(this.politicCards, user, game);
            // buy permit card, here you can buy permit
            PermitDeck permitDeck = game.getPermitDeck(region.getRegion());
            PermitCard permitCardToBuy = permitDeck.getAndRemovePermitCardVisible(this.permitCard);
            permitCardToBuy.getBonus().getBonus(user, game);
            user.addPermitCard(permitCardToBuy);
            this.removeAction(game, user);
        } else {
            throw new ActionNotPossibleException(Constants.POLITIC_CARD_EXCEPTION);
        }
    }

    @Override
    public String toString() {
        return "[MAIN ACTION] Buy permit card " + this.permitCard.getCityAcronimous() + " of region: " + this.userRegion.getRegion() + "";
    }

}
