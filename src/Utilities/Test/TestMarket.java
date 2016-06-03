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

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by Emanuele on 03/06/2016.
 */
public class TestMarket {
    User user;

    @Before
    public void before(){
        GamesManager gameController = GamesManager.getInstance();
        user = new User(new RMICommunication(),gameController);
        gameController.addToGame(user);
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


}
