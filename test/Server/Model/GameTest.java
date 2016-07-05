package Server.Model;

import CommonModel.GameModel.Bonus.Reward.KingBonusCard;
import Server.Controller.GamesManager;
import Server.NetworkInterface.Communication.FakeCommunication;
import Utilities.Class.Constants;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Emanuele on 29/06/2016.
 */
public class GameTest {

    private Game game;
    private User user;

    @Before
    public void setUp() throws Exception {
        this.game = new Game();
        try {
            this.user = new User(new FakeCommunication(), GamesManager.getInstance());
        } catch (Exception e) {

        }
        this.user.setVictoryPathPosition(0);
    }

    /**
     * Test king bonus card
     *
     * @throws Exception
     */
    @Test
    public void getKingBonusCard() throws Exception {
        KingBonusCard kingBonusCard = this.game.getKingBonusCard();

        System.out.println(kingBonusCard);

        kingBonusCard.getBonus(this.user, this.game);

        assertEquals(this.user.getVictoryPathPosition(), Constants.FIRST_ARRIVED_KING_BONUS);
    }

}