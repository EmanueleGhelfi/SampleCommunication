package CommonModel.GameModel.Council;

import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticColor;
import CommonModel.GameModel.City.RegionName;
import Server.Controller.GamesManager;
import Server.Model.Game;
import Server.Model.User;
import Server.NetworkInterface.Communication.FakeCommunication;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by Emanuele on 29/06/2016.
 */
public class CouncilTest {

    Game game;
    User user;

    @Before
    public void setUp() throws Exception {

        game = new Game();
        try {
            user= new User(new FakeCommunication(), GamesManager.getInstance());
        }
        catch (Exception e){

        }
        user.setVictoryPathPosition(0);
    }


    /**
     * test removes the head of the queue
     */
    @Test
    public void testAddLast(){
        Council council = game.getRegion(RegionName.HILL).getCouncil();

        Councilor councilor= new Councilor(PoliticColor.BLACK);

        Councilor first = council.getCouncil().peek();

        council.add(councilor);

        assertNotSame(first, council.getCouncil().peek());
    }


    /**
     *
     */
    @Test
    public void testAddFirst(){
        Council council = game.getRegion(RegionName.HILL).getCouncil();

        Councilor councilor= new Councilor(PoliticColor.BLACK);

        Councilor first = council.getCouncil().peek();

        council.add(councilor);

        ArrayList<Councilor> councilorArray = new ArrayList<>(council.getCouncil());

        assertTrue(council.getCouncil().contains(councilor));

        assertEquals(councilor,councilorArray.get(councilorArray.size()-1));

    }
}