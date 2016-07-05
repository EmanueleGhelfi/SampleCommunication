package Utilities.Test;

import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticCard;
import CommonModel.GameModel.Market.BuyableWrapper;
import Server.Controller.GamesManager;
import Server.Model.User;
import Server.NetworkInterface.Communication.RMICommunication;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by Emanuele on 03/06/2016.
 */
public class TestMarket {
    User user;
    User user2;

    @Before
    public void before() {
        GamesManager gameController = GamesManager.getInstance();
        this.user = new User(new RMICommunication(), gameController);
        this.user2 = new User(new RMICommunication(), gameController);
        gameController.addToGame(this.user);
        gameController.addToGame(this.user2);
        this.user.getGameController().notifyStarted();
    }

    @Test
    public void testBuy() {
        PoliticCard politicCard = this.user.getPoliticCards().get(0);
        ArrayList<BuyableWrapper> buyableWrappers = new ArrayList<>();
        BuyableWrapper buyableWrapper = new BuyableWrapper(politicCard, 2, this.user.getUsername());
        buyableWrappers.add(buyableWrapper);
        this.user.getGameController().onReceiveBuyableObject(buyableWrappers);
        assertEquals(this.user.getGame().getMarketList().get(0), buyableWrapper);
    }

    @Test
    public void testSell() {

        this.testBuy();
        assertEquals(this.user.getGame().getMarketList().size(), 1);
        PoliticCard politicCard = this.user.getPoliticCards().get(0);
        BuyableWrapper buyableWrapper = new BuyableWrapper(politicCard, 2, this.user.getUsername());
        this.user.getGameController().onRemoveItem(buyableWrapper);
        assertEquals(this.user.getGame().getMarketList().size(), 0);
    }

    @Test
    public void testHelper() {
        ArrayList<BuyableWrapper> buyableWrappers = new ArrayList<>();
        this.user2.setHelpers(3);
        this.user.setHelpers(1);
        BuyableWrapper buyableWrapper = new BuyableWrapper(this.user2.getHelpers().get(0), 3, this.user2.getUsername());
        BuyableWrapper buyableWrapper1 = new BuyableWrapper(this.user2.getHelpers().get(1), 3, this.user2.getUsername());
        buyableWrappers.add(buyableWrapper);
        buyableWrappers.add(buyableWrapper1);
        this.user2.getGameController().onReceiveBuyableObject(buyableWrappers);
        assertEquals(this.user2.getGame().getMarketList().size(), 2);
        ArrayList<BuyableWrapper> buyableWrappersToBuy = new ArrayList<>();
        buyableWrappersToBuy.add(buyableWrapper1);

        this.user.getGameController().onBuyObject(this.user, buyableWrappersToBuy);
        assertEquals(this.user.getHelpers().size(), 2);
    }


}
