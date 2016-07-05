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
        this.game = new Game();
        try {
            this.user = new User(new FakeCommunication(), GamesManager.getInstance());
        } catch (Exception e) {

        }
        this.user.setGame(this.game);
        this.user.setFastActionCounter(4);
    }

    @Test
    public void testAction() throws ActionNotPossibleException {
        this.user.setCoinPathPosition(10);
        this.user.setHelpers(10);
        int coin = this.user.getCoinPathPosition();
        int helper = this.user.getHelpers().size();
        int fastAction = this.user.getFastActionCounter();
        Action action = new FastActionMoneyForHelper();
        action.doAction(this.game, this.user);
        assertEquals(coin - Constants.MONEY_LIMITATION_MONEY_FOR_HELPER, this.user.getCoinPathPosition());
        assertEquals(helper + 1, this.user.getHelpers().size());
        assertEquals(fastAction - 1, this.user.getFastActionCounter());
    }

    @Test(expected = ActionNotPossibleException.class)
    public void testMoneyNotEnough() throws ActionNotPossibleException {
        this.user.setCoinPathPosition(0);
        int coin = this.user.getCoinPathPosition();
        int helper = this.user.getHelpers().size();
        int fastAction = this.user.getFastActionCounter();
        Action action = new FastActionMoneyForHelper();
        action.doAction(this.game, this.user);
        assertEquals(coin, this.user.getCoinPathPosition());
        assertEquals(helper, this.user.getHelpers().size());
        assertEquals(fastAction, this.user.getFastActionCounter());
    }
}