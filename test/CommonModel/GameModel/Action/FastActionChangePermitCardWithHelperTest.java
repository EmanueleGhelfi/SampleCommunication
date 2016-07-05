package CommonModel.GameModel.Action;

import CommonModel.GameModel.Card.SingleCard.PermitCard.PermitCard;
import CommonModel.GameModel.City.RegionName;
import Server.Controller.GamesManager;
import Server.Model.Game;
import Server.Model.User;
import Server.NetworkInterface.Communication.FakeCommunication;
import Utilities.Exception.ActionNotPossibleException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertNotSame;

/**
 * Created by Emanuele on 29/06/2016.
 */
public class FastActionChangePermitCardWithHelperTest {

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
        this.user.setUsername("MANU");
        this.user.setVictoryPathPosition(0);
        this.user.setFastActionCounter(2);

    }

    @Test
    public void testChangePermitCards() {
        this.user.setHelpers(20);
        ArrayList<PermitCard> permitCards = this.game.getPermitDeck(RegionName.HILL).getVisibleArray();
        Action action = new FastActionChangePermitCardWithHelper(RegionName.HILL);
        try {
            action.doAction(this.game, this.user);
            assertNotSame(permitCards.get(0), this.game.getPermitDeck(RegionName.HILL).getVisibleArray().get(0));
        } catch (ActionNotPossibleException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = ActionNotPossibleException.class)
    public void testNull() throws ActionNotPossibleException {
        this.user.setHelpers(20);
        Action action = new FastActionChangePermitCardWithHelper(null);
        action.doAction(this.game, this.user);
    }

    @Test(expected = ActionNotPossibleException.class)
    public void helperExc() throws ActionNotPossibleException {
        this.user.setHelpers(0);
        Action action = new FastActionChangePermitCardWithHelper(RegionName.HILL);
        action.doAction(this.game, this.user);
    }
}