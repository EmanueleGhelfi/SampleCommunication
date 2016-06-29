package CommonModel.GameModel.Action;

import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticColor;
import CommonModel.GameModel.City.RegionName;
import CommonModel.GameModel.Council.Councilor;
import Server.Controller.GamesManager;
import Server.Model.Game;
import Server.Model.User;
import Server.NetworkInterface.Communication.FakeCommunication;
import Utilities.Class.Constants;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Emanuele on 29/06/2016.
 */
public class FastActionElectCouncilorWithHelperTest {

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
        user.setHelpers(4);
        user.setFastActionCounter(4);
    }

    @Test
    public void testElectMain() throws Exception {
        int initFast = user.getFastActionCounter();
        Action action = new FastActionElectCouncilorWithHelper(RegionName.HILL,null,new Councilor(PoliticColor.BLACK), Constants.REGION_COUNCIL);
        action.doAction(game,user);
        assertEquals(3,user.getHelpers().size());
        assertEquals(user.getFastActionCounter(),initFast-1);
    }
}