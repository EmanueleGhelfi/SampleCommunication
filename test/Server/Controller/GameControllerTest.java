package Server.Controller;

import CommonModel.GameModel.Market.BuyableObject;
import CommonModel.GameModel.Market.BuyableWrapper;
import Server.Model.Game;
import Server.Model.User;
import Server.NetworkInterface.Communication.FakeCommunication;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by Emanuele on 27/06/2016.
 */
public class GameControllerTest {

    private Game game;
    private User user;
    private User enemy;
    private GameController gameController;

    @Before
    public void setUp() throws Exception {
        game = new Game();
        user = new User(new FakeCommunication(), GamesManager.getInstance());
        enemy = new User(new FakeCommunication(),GamesManager.getInstance());
        user.setGame(game);
        user.setHelpers(5);
        game.addUserToGame(user);
        game.addUserToGame(enemy);
        gameController = game.getGameController();
    }


    @Test
    public void testMarket() throws Exception {
        ArrayList<BuyableWrapper> buyableWrappers = new ArrayList<>();
        buyableWrappers.add(new BuyableWrapper(user.getHelpers().get(0),"Manu"));
        gameController.onReceiveBuyableObject(buyableWrappers);

        assertTrue(game.getMarketList().contains(buyableWrappers.get(0)));
    }
}