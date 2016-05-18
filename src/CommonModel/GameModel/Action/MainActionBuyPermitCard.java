package CommonModel.GameModel.Action;

import CommonModel.GameModel.Bonus.Generic.MainBonus;
import CommonModel.GameModel.Card.Deck.PoliticDeck;
import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticColor;
import CommonModel.GameModel.Council.Council;
import CommonModel.GameModel.Council.Councilor;
import Server.Controller.GamesManager;
import Server.NetworkInterface.Communication.RMICommunication;
import Server.NetworkInterface.Communication.SocketCommunication;
import Utilities.Class.Constants;
import Utilities.Exception.ActionNotPossibleException;
import CommonModel.GameModel.Card.Deck.PermitDeck;
import CommonModel.GameModel.Card.SingleCard.PermitCard.PermitCard;
import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticCard;
import CommonModel.GameModel.City.Region;
import Server.Model.Game;
import Server.Model.User;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

/**
 * Created by Emanuele on 16/05/2016.
 */
public class MainActionBuyPermitCard extends Action {


    private Region userRegion;
    private ArrayList<PoliticCard> politicCards;
    private PermitCard permitCard;
    private int bonusCounter = 0;



    public MainActionBuyPermitCard(ArrayList<PoliticCard> politicCard, Region userRegion, PermitCard permitCard) {
        this.politicCards = politicCard;
        this.userRegion = userRegion;
        this.type = Constants.MAIN_ACTION;
        this.permitCard = permitCard;
    }


    @Override
    public void doAction(Game game, User user) throws ActionNotPossibleException {

        // count number of correct politic card
        int correctPoliticCard = 0;
        // region of permit card
        Region region = game.getRegion(userRegion.getRegion());

        //this is the new position of the user in money path
        int newPositionInMoneyPath =0;

        // calculate correct politic card
        correctPoliticCard = countCorrectPoliticCard(region);

        // calculate money to spend
        newPositionInMoneyPath = calculateMoney(correctPoliticCard);

        // go ahead in money path
        game.getMoneyPath().goAhead(user,newPositionInMoneyPath);

        // re-add to game deck
        game.getPoliticCards().addToQueue(new HashSet<PoliticCard>(politicCards));
        // remove cards from user
        System.out.println("POLITICS CARD" + politicCards.size());
        System.out.println("USER CARD" + user.getPoliticCards().size());
        int j =0;
       for(int i = 0; i< politicCards.size();i++){
           if(politicCards.get(i).equals(user.getPoliticCards().get(j))){
               user.getPoliticCards().remove(j);
               user.decrementPoliticCardNumber();
           }
           else{
               j++;
           }
       }

        // buy permit card, here you can buy permit
        PermitDeck permitDeck = game.getPermitDeck(region);
        PermitCard permitCardToBuy = permitDeck.getPermitCardVisible(permitCard);
        permitCardToBuy.getBonus().getBonus(user,game);
        user.addPermitCard(permitCardToBuy);
        removeAction(game, user);
    }

    private int calculateMoney(int correctPoliticCard) throws ActionNotPossibleException {
        // calculate money
        int newPositionInMoneyPath = 0;
        if(correctPoliticCard == politicCards.size()){
            if(correctPoliticCard<4 && correctPoliticCard>0)
                newPositionInMoneyPath=10-(correctPoliticCard-1);
            else if(correctPoliticCard==4)
                newPositionInMoneyPath = 0;
            newPositionInMoneyPath+=bonusCounter;
        }
        else {
            throw new ActionNotPossibleException();
        }
        System.out.println("NUOVA POS "+correctPoliticCard);
        return newPositionInMoneyPath;
    }

    private int countCorrectPoliticCard(Region region) {
        int correctPoliticCard =0;
        // count all correct and bonus card
        Queue<Councilor> council = region.getCouncil().getCouncil();
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


    public static void main(String[] args){
        Game game = new Game();
        User user = null;
        try {
            user = new User(new RMICommunication("name"), GamesManager.getInstance());
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        System.out.println(user);

        ArrayList<PoliticCard> politicCardArrayList = new ArrayList<>();
        politicCardArrayList.add(new PoliticCard(PoliticColor.WHITE,false));
        politicCardArrayList.add(new PoliticCard(PoliticColor.WHITE,false));
        politicCardArrayList.add(new PoliticCard(PoliticColor.ORANGE,false));

        user.getPoliticCards().addAll(politicCardArrayList);

        Region region = Region.MOUNTAIN;


        PermitCard permitCard = new PermitCard(new MainBonus(1,5,9,false),null,Region.MOUNTAIN);

        MainActionBuyPermitCard mainActionBuyPermitCard = new MainActionBuyPermitCard(politicCardArrayList,region,permitCard);

        try {
            mainActionBuyPermitCard.doAction(game,user);
        } catch (ActionNotPossibleException e) {
            e.printStackTrace();
        }
    }

}
