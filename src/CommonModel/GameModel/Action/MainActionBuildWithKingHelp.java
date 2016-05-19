package CommonModel.GameModel.Action;

import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticCard;
import CommonModel.GameModel.City.City;
import CommonModel.GameModel.City.CityVisitor;
import CommonModel.GameModel.Council.King;
import Server.Model.Game;
import Server.Model.User;
import Utilities.Class.Constants;
import Utilities.Exception.ActionNotPossibleException;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Giulio on 18/05/2016.
 */
public class MainActionBuildWithKingHelp extends Action {

    private ArrayList<City> kingPath;
    private ArrayList<PoliticCard> politicCards;
    private int bonusCounter = 0;

    public MainActionBuildWithKingHelp(ArrayList<City> kingPath, ArrayList<PoliticCard> politicCards) {
        this.kingPath = kingPath;
        this.politicCards = politicCards;
    }

    //TODO
    @Override
    public void doAction(Game game, User user) throws ActionNotPossibleException {
        // count number of correct politic card
        int correctPoliticCard = 0;
        // region of permit card
        King king = game.getKing();
        // city where king goes
        City kingCity = kingPath.get(kingPath.size()-1);
        //this is the new position of the user in money path
        int newPositionInMoneyPath = 0;
        // calculate correct politic card
        correctPoliticCard = countCorrectPoliticCard(king, politicCards, bonusCounter);
        // calculate money to spend
        newPositionInMoneyPath = calculateMoney(correctPoliticCard, politicCards, bonusCounter);
        // go ahead in money path
        game.getMoneyPath().goAhead(user,newPositionInMoneyPath);
        // re-add to game deck
        game.getPoliticCards().addToQueue(new HashSet<>(politicCards));
        // remove cards from user
        System.out.println("POLITICS CARD" + politicCards.size());
        System.out.println("USER CARD" + user.getPoliticCards().size());
        getPoliticCard(politicCards, user);
        if (kingPath.size()*Constants.KING_PRICE < user.getCoinPathPosition()){
            for (City city: kingPath){
                user.setCoinPathPosition(user.getCoinPathPosition()-Constants.KING_PRICE);
                king.setCurrentCity(city);
            }
            user.addEmporium(kingCity);
            kingCity.getBonus().getBonus(user, game);
            CityVisitor cityVisitor = new CityVisitor(game.getGraph(), user.getUsersEmporium());
            for (City cityToVisit : cityVisitor.visit(kingCity)) {
                cityToVisit.getBonus().getBonus(user, game);
            }
        } else {
            throw new ActionNotPossibleException();
        }
        removeAction(game, user);
    }

}
