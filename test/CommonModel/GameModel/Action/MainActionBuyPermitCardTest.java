package CommonModel.GameModel.Action;

import CommonModel.GameModel.Card.SingleCard.PermitCard.PermitCard;
import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticCard;
import CommonModel.GameModel.City.RegionName;
import CommonModel.GameModel.Council.Councilor;
import Server.Controller.GamesManager;
import Server.Model.Game;
import Server.Model.User;
import Server.NetworkInterface.Communication.FakeCommunication;
import Utilities.Exception.ActionNotPossibleException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Emanuele on 01/07/2016.
 */
public class MainActionBuyPermitCardTest {


    Game game;
    User user;

    @Before
    public void setUp() throws Exception {
        game = new Game();
        user = new User(new FakeCommunication(), GamesManager.getInstance());

    }

    @Test
    public void testBuy() throws Exception {
        user.setCoinPathPosition(20);
        user.setMainActionCounter(2);
        ArrayList<PoliticCard> politicCardArrayList = new ArrayList<>();
        ArrayList<Councilor> councilors = new ArrayList<Councilor>(game.getRegion(RegionName.MOUNTAIN).getCouncil().getCouncil());
        for (Councilor councilor : councilors) {
            PoliticCard politicCard = new PoliticCard(councilor.getColor(), false);
            politicCardArrayList.add(politicCard);
        }
        user.getPoliticCards().addAll(politicCardArrayList);
        RegionName region = RegionName.MOUNTAIN;
        PermitCard permitCard = game.getPermitDeck(RegionName.MOUNTAIN).getVisibleArray().get(0);
        MainActionBuyPermitCard mainActionBuyPermitCard = new MainActionBuyPermitCard(politicCardArrayList, region, permitCard);
        mainActionBuyPermitCard.doAction(game, user);

        assertFalse(game.getPermitDeck(RegionName.MOUNTAIN).getVisibleArray().contains(permitCard));

        assertTrue(user.getPermitCards().contains(permitCard));

    }

    @Test(expected = ActionNotPossibleException.class)
    public void testNotPresent() throws ActionNotPossibleException {
        user.setCoinPathPosition(20);
        user.setMainActionCounter(2);
        ArrayList<PoliticCard> politicCardArrayList = new ArrayList<>();
        ArrayList<Councilor> councilors = new ArrayList<Councilor>(game.getRegion(RegionName.MOUNTAIN).getCouncil().getCouncil());
        for (Councilor councilor : councilors) {
            PoliticCard politicCard = new PoliticCard(councilor.getColor(), false);
            politicCardArrayList.add(politicCard);
        }
        user.getPoliticCards().addAll(politicCardArrayList);
        RegionName region = RegionName.MOUNTAIN;
        PermitCard permitCard = game.getPermitDeck(RegionName.HILL).getVisibleArray().get(0);
        MainActionBuyPermitCard mainActionBuyPermitCard = new MainActionBuyPermitCard(politicCardArrayList, region, permitCard);
        mainActionBuyPermitCard.doAction(game, user);

    }
}