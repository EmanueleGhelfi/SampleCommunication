package CommonModel.GameModel.Path;

import Server.Controller.GamesManager;
import Server.Model.Game;
import Server.Model.User;
import Server.NetworkInterface.Communication.FakeCommunication;
import Utilities.Class.Constants;
import Utilities.Exception.ActionNotPossibleException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.awt.*;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

/**
 * Created by Emanuele on 06/07/2016.
 */
@RunWith(Parameterized.class)
public class VictoryPathTest {


    private Game game;
    private User user;

    private int expected;
    private int value;

    @Parameterized.Parameters
    public static Collection<Object[]> data(){
        return Arrays.asList(new Object[][]{
                {0,0},{1,1},{5,5},{20,20},{-10,0},{25,25},{100, Constants.VICTORY_PATH_LENGTH}
        });
    }

    public VictoryPathTest(int value, int expected) {
        this.expected=expected;
        this.value = value;
    }



    @Before
    public void setUp() throws Exception {
        game = new Game();
        user = new User(new FakeCommunication(), GamesManager.getInstance());
        user.setGame(game);
        user.setVictoryPathPosition(0);
    }


    @Test
    public void testGoAhead() throws Exception {

        user.setVictoryPathPosition(0);
        try {
            game.getVictoryPath().goAhead(user, value);
            assertEquals(expected, user.getVictoryPathPosition());
        }
        catch (Exception e){

        }

    }

    @Test
    public void testException() throws ActionNotPossibleException {

        game.getVictoryPath().goAhead(user,110);
        assertEquals(user.getVictoryPathPosition(),Constants.VICTORY_PATH_LENGTH);
    }



    @Test (expected = ActionNotPossibleException.class)
    public void testUnderException() throws ActionNotPossibleException {

        user.setVictoryPathPosition(0);
        game.getVictoryPath().goAhead(user,-5);

    }


}