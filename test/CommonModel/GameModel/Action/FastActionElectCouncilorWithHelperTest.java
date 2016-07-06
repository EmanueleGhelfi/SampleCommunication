package CommonModel.GameModel.Action;

import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticColor;
import CommonModel.GameModel.City.City;
import CommonModel.GameModel.City.CityName;
import CommonModel.GameModel.City.Color;
import CommonModel.GameModel.City.RegionName;
import CommonModel.GameModel.Council.Councilor;
import CommonModel.GameModel.Council.King;
import Server.Controller.GamesManager;
import Server.Model.Game;
import Server.Model.User;
import Server.NetworkInterface.Communication.FakeCommunication;
import Utilities.Class.Constants;
import Utilities.Exception.ActionNotPossibleException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

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
            user = new User(new FakeCommunication(), GamesManager.getInstance());
        } catch (Exception e) {

        }
        user.setGame(game);
        user.setFastActionCounter(4);
    }

    @Test
    public void testElectMain() throws Exception {
        user.setHelpers(4);
        int initHelper = user.getHelpers().size();
        int initFast = user.getFastActionCounter();
        Action action = new FastActionElectCouncilorWithHelper(RegionName.HILL, null, new Councilor(PoliticColor.BLACK), Constants.REGION_COUNCIL);
        action.doAction(game, user);
        assertEquals(initHelper - 1, user.getHelpers().size());
        assertEquals(user.getFastActionCounter(), initFast - 1);
        ArrayList<Councilor> councilors = new ArrayList<>(game.getRegion(RegionName.HILL).getCouncil().getCouncil());
        assertEquals(new Councilor(PoliticColor.BLACK), councilors.get(councilors.size() - 1));
    }

    @Test
    public void testElectKing() throws ActionNotPossibleException {
        King king = new King(new City(Color.BLUE, CityName.CASTRUM, RegionName.COAST), game.getBank());
        game.setKing(king);
        user.setHelpers(4);
        int initHelper = user.getHelpers().size();
        int initFast = user.getFastActionCounter();
        Action action = new FastActionElectCouncilorWithHelper(RegionName.HILL, king, new Councilor(PoliticColor.BLACK), Constants.KING_COUNCIL);
        action.doAction(game, user);
        assertEquals(initHelper - 1, user.getHelpers().size());
        assertEquals(user.getFastActionCounter(), initFast - 1);
        ArrayList<Councilor> councilors = new ArrayList<>(game.getKing().getCouncil().getCouncil());
        assertEquals(new Councilor(PoliticColor.BLACK), councilors.get(councilors.size() - 1));
    }


    @Test(expected = ActionNotPossibleException.class)
    public void testActionNotPossible() throws Exception {
        user.setHelpers(0);
        int initFast = user.getFastActionCounter();
        Action action = new FastActionElectCouncilorWithHelper(RegionName.HILL, null, new Councilor(PoliticColor.BLACK), Constants.REGION_COUNCIL);
        action.doAction(game, user);
        assertEquals(user.getFastActionCounter(), initFast);
    }

    @Test(expected = ActionNotPossibleException.class)
    public void testNull() throws ActionNotPossibleException {
        user.setHelpers(4);
        int initHelper = user.getHelpers().size();
        int initFast = user.getFastActionCounter();
        Action action = new FastActionElectCouncilorWithHelper(RegionName.HILL, null, null, Constants.REGION_COUNCIL);
        action.doAction(game, user);
        assertEquals(user.getFastActionCounter(), initFast);
    }
}