package Utilities.Test;

import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticCard;
import CommonModel.GameModel.Market.BuyableWrapper;
import Server.Controller.GamesManager;
import Server.Model.Game;
import Server.Model.User;
import Server.NetworkInterface.Communication.RMICommunication;

import org.junit.Before;
import org.junit.Test;
import org.junit.internal.ArrayComparisonFailure;

import javax.jws.soap.SOAPBinding;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by Emanuele on 03/06/2016.
 */
public class TestMarket {
    User user;
    User user2;

    @Before
    public void before(){
        GamesManager gameController = GamesManager.getInstance();
        user = new User(new RMICommunication(),gameController);
        user2 = new User(new RMICommunication(),gameController);
        gameController.addToGame(user);
        gameController.addToGame(user2);
        user.getGameController().notifyStarted();
    }

    @Test
    public void testBuy(){
        PoliticCard politicCard = user.getPoliticCards().get(0);
        ArrayList<BuyableWrapper> buyableWrappers = new ArrayList<>();
        BuyableWrapper buyableWrapper = new BuyableWrapper(politicCard,2,user.getUsername());
        buyableWrappers.add(buyableWrapper);
        user.getGameController().onReceiveBuyableObject(buyableWrappers);
        assertEquals(user.getGame().getMarketList().get(0),buyableWrapper);
    }

    @Test
    public void testSell(){

        testBuy();
        assertEquals(user.getGame().getMarketList().size(),1);
        PoliticCard politicCard = user.getPoliticCards().get(0);
        BuyableWrapper buyableWrapper = new BuyableWrapper(politicCard,2,user.getUsername());
        user.getGameController().onRemoveItem(buyableWrapper);
        assertEquals(user.getGame().getMarketList().size(),0);
    }

    @Test
    public void testHelper(){
        ArrayList<BuyableWrapper> buyableWrappers = new ArrayList<>();
        user2.setHelpers(3);
        user.setHelpers(1);
        BuyableWrapper buyableWrapper = new BuyableWrapper(user2.getHelpers().get(0),3,user2.getUsername());
        BuyableWrapper buyableWrapper1 = new BuyableWrapper(user2.getHelpers().get(1),3,user2.getUsername());
        buyableWrappers.add(buyableWrapper);
        buyableWrappers.add(buyableWrapper1);
        user2.getGameController().onReceiveBuyableObject(buyableWrappers);
        assertEquals(user2.getGame().getMarketList().size(),2);
        ArrayList<BuyableWrapper> buyableWrappersToBuy = new ArrayList<>();
        buyableWrappersToBuy.add(buyableWrapper1);

        user.getGameController().onBuyObject(user,buyableWrappersToBuy);
        assertEquals(user.getHelpers().size(),2);
    }


}
