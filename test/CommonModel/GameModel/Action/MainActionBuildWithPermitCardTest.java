package CommonModel.GameModel.Action;

import CommonModel.GameModel.Card.SingleCard.PermitCard.PermitCard;
import CommonModel.GameModel.City.City;
import CommonModel.GameModel.City.RegionName;
import CommonModel.GameModel.Council.King;
import Server.Controller.GamesManager;
import Server.Model.Game;
import Server.Model.Map;
import Server.Model.User;
import Server.NetworkInterface.Communication.FakeCommunication;
import Utilities.Class.Constants;
import Utilities.Exception.ActionNotPossibleException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Emanuele on 01/07/2016.
 */
public class MainActionBuildWithPermitCardTest {

    private Game game;
    private User user;

    @Before
    public void setUp() throws Exception {
        game = new Game();
        user = new User(new FakeCommunication(), GamesManager.getInstance());
        user.setGame(game);
        user.setUsername("MANU");
        user.setVictoryPathPosition(0);
        user.setFastActionCounter(2);

        user.setFastActionCounter(4);
        user.setMainActionCounter(4);

        user.setNobilityPathPosition(game.getNobilityPath().getPosition()[Constants.INITIAL_POSITION_ON_NOBILITY_PATH]);

        Map map = Map.readAllMap().get(0);
        game.setMap(map);

        King king = new King(game.getMap().getLinks().get(0).getCity1(), game.getBank());
        game.setKing(king);

        game.addUserToGame(user);

    }

    @Test
    public void testBuyPermit() throws Exception {

        City cityToBuild = null;

        PermitCard permitCard = game.getPermitDeck(RegionName.HILL).getVisibleArray().get(0);

        user.addPermitCard(permitCard);

        for (City city : game.getMap().getCity()) {
            if (city.getCityName().getCityName().charAt(0) == permitCard.getCityAcronimous().get(0)) {
                cityToBuild = city;
                break;
            }
        }

        Action action = new MainActionBuildWithPermitCard(cityToBuild, permitCard);
        action.doAction(game, user);

        assertTrue(user.getUsersEmporium().contains(cityToBuild));

        assertTrue(user.getOldPermitCards().contains(permitCard));

        assertFalse(user.getPermitCards().contains(permitCard));

    }

    @Test(expected = ActionNotPossibleException.class)
    public void testNull() throws ActionNotPossibleException {
        City cityToBuild = null;

        PermitCard permitCard = null;


        Action action = new MainActionBuildWithPermitCard(cityToBuild, permitCard);
        action.doAction(game, user);

    }

    @Test(expected = ActionNotPossibleException.class)
    public void testPermitNotPresent() throws ActionNotPossibleException {
        City cityToBuild = null;

        PermitCard permitCard = game.getPermitDeck(RegionName.HILL).getVisibleArray().get(0);


        for (City city : game.getMap().getCity()) {
            if (city.getCityName().getCityName().charAt(0) == permitCard.getCityAcronimous().get(0)) {
                cityToBuild = city;
                break;
            }
        }

        Action action = new MainActionBuildWithPermitCard(cityToBuild, permitCard);
        action.doAction(game, user);

    }
}