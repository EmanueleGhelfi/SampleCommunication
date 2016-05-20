package CommonModel.GameModel.Action;

import CommonModel.GameModel.Bonus.Reward.KingBonusCard;
import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticCard;
import CommonModel.GameModel.City.City;
import CommonModel.GameModel.Council.Councilor;
import CommonModel.GameModel.Council.GotCouncil;
import Utilities.Class.Constants;
import Utilities.Exception.ActionNotPossibleException;
import Server.Model.Game;
import Server.Model.User;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Queue;

/**
 * Created by Emanuele on 16/05/2016.
 */
public abstract class Action implements Serializable {

    protected String type;

    public abstract void doAction(Game game, User user) throws ActionNotPossibleException;

    void removeAction(Game game,User user){
        switch (type) {
            case Constants.MAIN_ACTION:
                user.setMainActionCounter(user.getMainActionCounter()-1);
                break;
            case Constants.FAST_ACTION:
                user.setFastActionCounter(user.getFastActionCounter()-1);
                break;
        }
    }

    protected void getPoliticCard (ArrayList<PoliticCard> politicCards, User user){
        int cont2 =0;
        for(int cont1 = 0; cont1< politicCards.size();cont1++){
            if(politicCards.get(cont1).equals(user.getPoliticCards().get(cont2))){
                user.getPoliticCards().remove(cont2);
                user.decrementPoliticCardNumber();
            }
            else{
                cont2++;
            }
        }
    }

    protected void checkRegionBonus (City city, User user, Game game) throws ActionNotPossibleException {
        if (city.getRegion().checkRegion(user.getUsersEmporium())){
            game.getRegionBonusCard(city.getRegion().getRegion()).getBonus(user, game);
            // check king bonus and get it
            KingBonusCard kingBonusCard = game.getKingBonusCard();
            if (kingBonusCard != null){
                kingBonusCard.getBonus(user, game);
            }
        }
    }

    protected void checkColorBonus (City city, User user, Game game) throws ActionNotPossibleException{
        if (city.getColor().checkColor(user.getUsersEmporium())){
            game.getColorBonusCard(city.getColor().getColor()).getBonus(user, game);
            // check king bonus and get it
            KingBonusCard kingBonusCard = game.getKingBonusCard();
            if (kingBonusCard != null){
                kingBonusCard.getBonus(user, game);
            }
        }
    }

    protected int calculateMoney(int correctPoliticCard, ArrayList<PoliticCard> politicCards, int bonusCounter) throws ActionNotPossibleException {
        // calculate money
        int newPositionInMoneyPath = 0;
        if(correctPoliticCard == politicCards.size()){
            if(correctPoliticCard<Constants.FOUR_PARAMETER_BUY_PERMIT_CARD && correctPoliticCard>0)
                newPositionInMoneyPath=Constants.TEN_PARAMETER_BUY_PERMIT_CARD -(correctPoliticCard-Constants.ONE_PARAMETER_BUY_PERMIT_CARD);
            else if(correctPoliticCard==Constants.FOUR_PARAMETER_BUY_PERMIT_CARD)
                newPositionInMoneyPath = 0;
            newPositionInMoneyPath+=bonusCounter;
        }
        else {
            throw new ActionNotPossibleException();
        }
        System.out.println("NUOVA POS "+correctPoliticCard);
        return newPositionInMoneyPath;
    }

    protected int countCorrectPoliticCard(GotCouncil gotCouncil, ArrayList<PoliticCard> politicCards, int bonusCounter) {
        int correctPoliticCard =0;
        // count all correct and bonus card
        Queue<Councilor> council = gotCouncil.getCouncil().getCouncil();
        for (PoliticCard politicCard: politicCards) {
            if(politicCard.isMultiColor()){
                bonusCounter ++;
                correctPoliticCard++;
            }
            else{
                for (Councilor councilor: council) {
                    if(councilor.getColor().equals(politicCard.getPoliticColor())){
                        correctPoliticCard++;
                        council.remove(councilor);
                        break;
                    }
                }
            }
        }
        System.out.println("CARTE CORRETTE "+correctPoliticCard);
        return correctPoliticCard;
    }

    /** check if an user has placed more than 10 emporiums
     *
     */
    protected boolean checkEmporiumsAreNotTen(User user) throws ActionNotPossibleException {
        if (user.getUsersEmporium().size()>=Constants.EMPORIUMS_BUILDABLE){
            throw new ActionNotPossibleException();
        }
        return true;
    }

    protected boolean checkEmporiumsIsAlreadyPresent(User user, City cityWantToBuildIn) throws  ActionNotPossibleException{
        for(City city: user.getUsersEmporium()){
            if (cityWantToBuildIn.equals(city))
                throw new ActionNotPossibleException();
        }
        return true;
    }

    String getType(){
        return type;
    }
}
