package CommonModel.GameModel.Path;

import CommonModel.GameModel.Action.Action;
import Server.Controller.GamesManager;
import Server.Model.Game;
import Server.Model.User;
import Server.NetworkInterface.Communication.FakeCommunication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

/**
 * Created by Emanuele
 */
@RunWith(Parameterized.class)
public class MoneyPathTest {


    private Game game;
    private User user;

    private int expected;
    private int value;

    @Parameterized.Parameters
    public static Collection<Object[]> data(){
        return Arrays.asList(new Object[][]{
            {0,0},{1,1},{5,5},{20,20},{-10,0},{25,20}
        });
    }

    public MoneyPathTest(int value, int expected) {
        this.expected=expected;
        this.value = value;
    }



    @Before
    public void setUp() throws Exception {
        game = new Game();
        user = new User(new FakeCommunication(), GamesManager.getInstance());
        user.setGame(game);
        user.setUsername("MANU");
        user.setVictoryPathPosition(0);
        user.setFastActionCounter(2);

    }


    @Test
    public void testGoAhead() throws Exception {

        user.setCoinPathPosition(0);
        try {
            game.getMoneyPath().goAhead(user, value);
            assertEquals(expected, user.getCoinPathPosition());
        }
        catch (Exception e){

        }

    }
}