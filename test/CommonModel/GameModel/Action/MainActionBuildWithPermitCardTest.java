package CommonModel.GameModel.Action;

import CommonModel.GameModel.City.City;
import CommonModel.GameModel.Council.King;
import Server.Controller.GamesManager;
import Server.Model.Game;
import Server.Model.Map;
import Server.Model.User;
import Server.NetworkInterface.Communication.FakeCommunication;
import Utilities.Class.Constants;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Emanuele on 01/07/2016.
 */
public class MainActionBuildWithPermitCardTest {

    private Game game;
    private User user;

    @Before
    public void setUp() throws Exception {
        game = new Game();
        user= new User(new FakeCommunication(), GamesManager.getInstance());
        user.setGame(game);
        user.setUsername("MANU");
        user.setVictoryPathPosition(0);
        user.setFastActionCounter(2);

        user.setFastActionCounter(4);
        user.setMainActionCounter(4);

        user.setNobilityPathPosition(game.getNobilityPath().getPosition()[Constants.INITIAL_POSITION_ON_NOBILITY_PATH]);

        Map map = Map.readAllMap().get(0);
        game.setMap(map);

        King king = new King(game.getMap().getLinks().get(0).getCity1(),game.getBank());
        game.setKing(king);

        game.addUserToGame(user);

    }

    @Test
    public void testBuyPermit() throws Exception {
        
        City cityToBuild = game.getMap().getCity().get(0);
        
        //// TODO: 02/07/2016  

    }
}