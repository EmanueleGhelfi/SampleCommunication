package CommonModel.GameModel.Action;

import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticColor;
import CommonModel.GameModel.City.RegionName;
import CommonModel.GameModel.Council.Councilor;
import CommonModel.GameModel.Council.King;
import Server.Controller.GamesManager;
import Server.Model.Game;
import Server.Model.Map;
import Server.Model.User;
import Server.NetworkInterface.Communication.FakeCommunication;
import Utilities.Class.Constants;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;
/**
 * Created by Emanuele
 */
public class MainActionElectCouncilorTest {

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
        user.setVictoryPathPosition(0);
        user.setFastActionCounter(2);
        user.setMainActionCounter(2);
    }

    /**
     * Elect a councilor in a region
     * @throws Exception
     */
    @Test
    public void testElect() throws Exception {
        int mainAction = user.getMainActionCounter();
        int money = user.getCoinPathPosition();

        Councilor councilor = new Councilor(PoliticColor.VIOLET);

        ArrayList<Councilor> councilorsInit = new ArrayList<>(game.getRegion(RegionName.HILL).getCouncil().getCouncil());

        Action action = new MainActionElectCouncilor(councilor,null, RegionName.HILL);

        action.doAction(game,user);

        ArrayList<Councilor> councilors = new ArrayList<>(game.getRegion(RegionName.HILL).getCouncil().getCouncil());

        assertEquals(councilor,councilors.get(councilors.size()-1));

        assertEquals(mainAction-1,user.getMainActionCounter());

        assertEquals(money + Constants.MONEY_EARNED_ELECT_COUNCILOR,user.getCoinPathPosition());

        assertTrue(game.getBank().checkCouncilor(councilorsInit.get(0).getColor()));

    }

    @Test
    public void testElectCouncilorKing() throws Exception {
        int mainAction = user.getMainActionCounter();
        int money = user.getCoinPathPosition();

        Councilor councilor = new Councilor(PoliticColor.BLACK);

        Map map = Map.readAllMap().get(0);
        game.setMap(map);

        King king = new King(game.getMap().getCity().get(0),game.getBank());

        game.setKing(king);

        ArrayList<Councilor> councilorsInit = new ArrayList<>(game.getKing().getCouncil().getCouncil());

        Action action = new MainActionElectCouncilor(councilor,game.getKing(), null);

        action.doAction(game,user);

        ArrayList<Councilor> councilors = new ArrayList<>(game.getKing().getCouncil().getCouncil());

        assertEquals(councilor,councilors.get(councilors.size()-1));

        assertEquals(mainAction-1,user.getMainActionCounter());

        assertEquals(money + Constants.MONEY_EARNED_ELECT_COUNCILOR,user.getCoinPathPosition());

        assertTrue(game.getBank().checkCouncilor(councilorsInit.get(0).getColor()));

    }
}