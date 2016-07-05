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
public class FastActionNewMainActionTest {


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

        this.user.setFastActionCounter(1);
        this.user.setMainActionCounter(2);
        this.user.setHelpers(10);


        int helpers = this.user.getHelpers().size();
        int actionCounter = this.user.getMainActionCounter();
        int fastActionCounter = this.user.getFastActionCounter();


        Action action = new FastActionNewMainAction();
        action.doAction(this.game, this.user);
        assertEquals(actionCounter + 1, this.user.getMainActionCounter());
        assertEquals(fastActionCounter - 1, this.user.getFastActionCounter());
        assertEquals(helpers - Constants.HELPER_LIMITATION_NEW_MAIN_ACTION, this.user.getHelpers().size());
    }


    @Test(expected = ActionNotPossibleException.class)
    public void testActionNotPossible() throws ActionNotPossibleException {
        this.user.setFastActionCounter(0);
        this.user.setMainActionCounter(2);
        this.user.setHelpers(10);


        int helpers = this.user.getHelpers().size();
        int actionCounter = this.user.getMainActionCounter();
        int fastActionCounter = this.user.getFastActionCounter();


        Action action = new FastActionNewMainAction();
        action.doAction(this.game, this.user);
        assertEquals(actionCounter + 1, this.user.getMainActionCounter());
        assertEquals(fastActionCounter - 1, this.user.getFastActionCounter());
        assertEquals(helpers - Constants.HELPER_LIMITATION_NEW_MAIN_ACTION, this.user.getHelpers().size());
    }

    @Test(expected = ActionNotPossibleException.class)
    public void testHelperNotEnough() throws ActionNotPossibleException {
        this.user.setFastActionCounter(1);
        this.user.setMainActionCounter(2);
        this.user.setHelpers(0);


        int helpers = this.user.getHelpers().size();
        int actionCounter = this.user.getMainActionCounter();
        int fastActionCounter = this.user.getFastActionCounter();


        Action action = new FastActionNewMainAction();
        action.doAction(this.game, this.user);
        assertEquals(actionCounter + 1, this.user.getMainActionCounter());
        assertEquals(fastActionCounter - 1, this.user.getFastActionCounter());
        assertEquals(helpers - Constants.HELPER_LIMITATION_NEW_MAIN_ACTION, this.user.getHelpers().size());
    }
}