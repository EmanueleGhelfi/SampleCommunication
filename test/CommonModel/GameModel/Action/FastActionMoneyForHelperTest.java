package CommonModel.GameModel.Action;

import Server.Controller.GamesManager;
import Server.Model.Game;
import Server.Model.User;
import Server.NetworkInterface.Communication.FakeCommunication;
import Utilities.Class.Constants;
import Utilities.Exception.ActionNotPossibleException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Emanuele on 01/07/2016.
 */
public class FastActionMoneyForHelperTest {

    private Game game;
    private User user;

    @Before
    public void setUp() throws Exception {
        game = new Game();
        try {
            user = new User(new FakeCommunication(), GamesManager.getInstance());
        } catch (Exception e) {

        }
        user.setGame(game);
        user.setFastActionCounter(4);
    }

    @Test
    public void testAction() throws ActionNotPossibleException {
        user.setCoinPathPosition(10);
        user.setHelpers(10);
        int coin = user.getCoinPathPosition();
        int helper = user.getHelpers().size();
        int fastAction = user.getFastActionCounter();
        Action action = new FastActionMoneyForHelper();
        action.doAction(game, user);
        assertEquals(coin - Constants.MONEY_LIMITATION_MONEY_FOR_HELPER, user.getCoinPathPosition());
        assertEquals(helper + 1, user.getHelpers().size());
        assertEquals(fastAction - 1, user.getFastActionCounter());
    }

    @Test(expected = ActionNotPossibleException.class)
    public void testMoneyNotEnough() throws ActionNotPossibleException {
        user.setCoinPathPosition(0);
        int coin = user.getCoinPathPosition();
        int helper = user.getHelpers().size();
        int fastAction = user.getFastActionCounter();
        Action action = new FastActionMoneyForHelper();
        action.doAction(game, user);
        assertEquals(coin, user.getCoinPathPosition());
        assertEquals(helper, user.getHelpers().size());
        assertEquals(fastAction, user.getFastActionCounter());
    }
}