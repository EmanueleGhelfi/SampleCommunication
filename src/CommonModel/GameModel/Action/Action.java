package CommonModel.GameModel.Action;

import CommonModel.GameModel.Bonus.Reward.KingBonusCard;
import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticCard;
import CommonModel.GameModel.City.City;
import CommonModel.GameModel.City.CityVisitor;
import CommonModel.GameModel.Council.Councilor;
import CommonModel.GameModel.Council.GotCouncil;
import CommonModel.GameModel.Market.BuyableWrapper;
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

    protected String actionType;

    public abstract void doAction(Game game, User user) throws ActionNotPossibleException;

    boolean checkActionCounter(User user) throws ActionNotPossibleException{
        switch (actionType){
            case Constants.FAST_ACTION:
                if(user.getFastActionCounter()<=0){
                    throw new ActionNotPossibleException("You don't have fast action!");
                }
                else return true;
            case Constants.MAIN_ACTION:
                if(user.getMainActionCounter()<=0){
                    System.out.println("Not your turn");
                    throw new ActionNotPossibleException("You don't have main action!");
                }
                else return true;
            default:
                throw new ActionNotPossibleException("Action type unknown");
        }

    }

    void removeAction(Game game,User user){
        switch (actionType) {
            case Constants.MAIN_ACTION:
                user.setMainActionCounter(user.getMainActionCounter()-1);
                break;
            case Constants.FAST_ACTION:
                user.setFastActionCounter(user.getFastActionCounter()-1);
                break;
        }
        System.out.println("HERE I AM");
        // send a snapshot to all player
        game.getGameController().sendSnapshotToAll();
    }

    protected void removePoliticCard(ArrayList<PoliticCard> politicCards, User user, Game game){
        int cont2 =0;
        /*
        for(int cont1 = 0; cont1< politicCards.size();cont1++){
            if(politicCards.get(cont1).equals(user.getPoliticCards().get(cont2))){
                user.getPoliticCards().remove(cont2);
                user.decrementPoliticCardNumber();
                System.out.println("Decrementing politic card number");
            }
            else{
                cont2++;
            }
        }
        */

        for(int i = 0; i < politicCards.size(); i++){
            for(int j = 0; j< user.getPoliticCards().size();j++){
                if(politicCards.get(i).equals(user.getPoliticCards().get(j))){
                    //TODO: test
                    game.removeFromMarketList(new BuyableWrapper(user.getPoliticCards().get(j),user.getUsername()));
                    user.getPoliticCards().remove(j);
                    user.decrementPoliticCardNumber();
                    System.out.println("Decrementing politic card number");
                    break;
                }
            }
        }
    }

    protected void checkRegionBonus (City city, User user, Game game) throws ActionNotPossibleException {
        if (game.getRegion(city.getRegion()).checkRegion(user.getUsersEmporium())){
            game.getRegionBonusCard(city.getRegion()).getBonus(user, game);
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
                System.out.println("Eccomi dentro");
                System.out.println(kingBonusCard);
                kingBonusCard.getBonus(user, game);
            }
            else{
                System.out.println("Sono qua");
            }
        }
    }

    protected int calculateMoney(int correctPoliticCard, ArrayList<PoliticCard> politicCards, int bonusCounter) throws ActionNotPossibleException {
        // calculate multicolor:
        int bonusNumber=0;
        for(PoliticCard politicCard:politicCards)
            if(politicCard.isMultiColor()){
                bonusNumber++;
            }
        // calculate money
        int newPositionInMoneyPath = 0;
        if(correctPoliticCard == politicCards.size()){
            if(correctPoliticCard<Constants.FOUR_PARAMETER_BUY_PERMIT_CARD && correctPoliticCard>0)
                newPositionInMoneyPath=Constants.TEN_PARAMETER_BUY_PERMIT_CARD -
                        3*(correctPoliticCard-Constants.ONE_PARAMETER_BUY_PERMIT_CARD);
            else if(correctPoliticCard==Constants.FOUR_PARAMETER_BUY_PERMIT_CARD)
                newPositionInMoneyPath = 0;
            newPositionInMoneyPath+=bonusNumber;
        }
        else {
            throw new ActionNotPossibleException(Constants.POLITIC_CARD_EXCEPTION);
        }
        System.out.println("NUOVA POS "+newPositionInMoneyPath+" bonus "+ bonusNumber);
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
            throw new ActionNotPossibleException("Ten emporiums!!!");
        }
        return true;
    }

    protected boolean checkEmporiumsIsAlreadyPresent(User user, City cityWantToBuildIn) throws  ActionNotPossibleException{
        for(City city: user.getUsersEmporium()){
            if (cityWantToBuildIn.equals(city))
                throw new ActionNotPossibleException("Emporiums already present!");
        }
        return true;
    }

    String getActionType(){
        return actionType;
    }

    void getNearCityBonus(Game game, User user,City city) throws ActionNotPossibleException {
        CityVisitor cityVisitor = new CityVisitor(game.getMap().getMapGraph(), user.getUsersEmporium());
        for (City cityToVisit : cityVisitor.visit(city)) {
            City cityToGetBonus = game.getCity(cityToVisit);
            if (cityToGetBonus != null && cityToGetBonus.getBonus() != null) {
                //cityToVisit.getBonus().getBonus(user, game);
                if (!city.getColor().getColor().equals(Constants.PURPLE))
                    cityToGetBonus.getBonus(user,game);
            } else {
                System.out.println(" " + cityToVisit + " has null bonus or is null");
            }
        }
    }
}
