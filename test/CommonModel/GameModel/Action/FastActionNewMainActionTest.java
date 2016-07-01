package CommonModel.GameModel.Action;

import Server.Controller.GamesManager;
import Server.Model.Game;
import Server.Model.User;
import Server.NetworkInterface.Communication.FakeCommunication;
import Utilities.Class.Constants;
import Utilities.Exception.ActionNotPossibleException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Emanuele on 01/07/2016.
 */
public class FastActionNewMainActionTest {


    private Game game;
    private User user;


    @Before
    public void setUp() throws Exception {
        game = new Game();
        try {
            user= new User(new FakeCommunication(), GamesManager.getInstance());
        }
        catch (Exception e){

        }
        user.setGame(game);
        user.setFastActionCounter(4);
    }

    @Test
    public void testAction() throws ActionNotPossibleException {

        user.setFastActionCounter(1);
        user.setMainActionCounter(2);
        user.setHelpers(10);


        int helpers = user.getHelpers().size();
        int actionCounter = user.getMainActionCounter();
        int fastActionCounter = user.getFastActionCounter();


        Action action = new FastActionNewMainAction();
        action.doAction(game,user);
        assertEquals(actionCounter+1,user.getMainActionCounter());
        assertEquals(fastActionCounter-1,user.getFastActionCounter());
        assertEquals(helpers- Constants.HELPER_LIMITATION_NEW_MAIN_ACTION,user.getHelpers().size());
    }


    @Test(expected = ActionNotPossibleException.class)
    public void testActionNotPossible() throws ActionNotPossibleException{
        user.setFastActionCounter(0);
        user.setMainActionCounter(2);
        user.setHelpers(10);


        int helpers = user.getHelpers().size();
        int actionCounter = user.getMainActionCounter();
        int fastActionCounter = user.getFastActionCounter();


        Action action = new FastActionNewMainAction();
        action.doAction(game,user);
        assertEquals(actionCounter+1,user.getMainActionCounter());
        assertEquals(fastActionCounter-1,user.getFastActionCounter());
        assertEquals(helpers- Constants.HELPER_LIMITATION_NEW_MAIN_ACTION,user.getHelpers().size());
    }

    @Test(expected = ActionNotPossibleException.class)
    public void testHelperNotEnough() throws ActionNotPossibleException{
        user.setFastActionCounter(1);
        user.setMainActionCounter(2);
        user.setHelpers(0);


        int helpers = user.getHelpers().size();
        int actionCounter = user.getMainActionCounter();
        int fastActionCounter = user.getFastActionCounter();


        Action action = new FastActionNewMainAction();
        action.doAction(game,user);
        assertEquals(actionCounter+1,user.getMainActionCounter());
        assertEquals(fastActionCounter-1,user.getFastActionCounter());
        assertEquals(helpers- Constants.HELPER_LIMITATION_NEW_MAIN_ACTION,user.getHelpers().size());
    }
}