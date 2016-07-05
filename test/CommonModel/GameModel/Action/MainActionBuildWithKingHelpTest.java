package CommonModel.GameModel.Action;

import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticCard;
import CommonModel.GameModel.City.City;
import CommonModel.GameModel.Council.Councilor;
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

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by Emanuele on 01/07/2016.
 */
public class MainActionBuildWithKingHelpTest {


    private Game game;
    private User user;
    private User userEnemy;

    @Before
    public void setUp() throws Exception {

        this.game = new Game();
        this.user = new User(new FakeCommunication(), GamesManager.getInstance());
        this.user.setUsername("User");

        this.userEnemy = new User(new FakeCommunication(), GamesManager.getInstance());
        this.userEnemy.setGame(this.game);
        this.userEnemy.setUsername("UserEnemy");
        this.user.setGame(this.game);
        this.user.setFastActionCounter(4);
        this.user.setMainActionCounter(4);

        this.user.setNobilityPathPosition(this.game.getNobilityPath().getPosition()[Constants.INITIAL_POSITION_ON_NOBILITY_PATH]);

        Map map = Map.readAllMap().get(0);
        this.game.setMap(map);

        King king = new King(this.game.getMap().getLinks().get(0).getCity1(), this.game.getBank());
        this.game.setKing(king);

        this.game.addUserToGame(this.user);
        this.game.addUserToGame(this.userEnemy);
    }


    // king goes in correct city
    @Test(timeout = 200)
    public void testBuild() throws Exception {
        int coin;

        ArrayList<City> kingPath = new ArrayList<>();
        kingPath.add(this.game.getKing().getCurrentCity());
        kingPath.add(this.game.getMap().getLinks().get(0).getCity2());
        this.user.setCoinPathPosition(kingPath.size() * Constants.KING_PRICE);
        ArrayList<PoliticCard> politicCardArrayList = new ArrayList<>();
        ArrayList<Councilor> councilors = new ArrayList<>(this.game.getKing().getCouncil().getCouncil());

        for (Councilor councilor : councilors) {
            PoliticCard politicCard = new PoliticCard(councilor.getColor(), false);
            politicCardArrayList.add(politicCard);
            this.user.addPoliticCard(politicCard);
        }
        Action action = new MainActionBuildWithKingHelp(kingPath, politicCardArrayList);
        action.doAction(this.game, this.user);

        assertEquals(kingPath.get(kingPath.size() - 1), this.game.getKing().getCurrentCity());

        assertEquals(0, this.user.getPoliticCards().size());

        assertEquals(kingPath.get(kingPath.size() - 1), this.user.getUsersEmporium().get(0));
    }

    //action not possible because of coin
    @Test(expected = ActionNotPossibleException.class)
    public void testActionNotPossible() throws ActionNotPossibleException {
        this.user.setCoinPathPosition(0);

        ArrayList<City> kingPath = new ArrayList<>();
        kingPath.add(this.game.getKing().getCurrentCity());
        kingPath.add(this.game.getMap().getLinks().get(7).getCity2());
        this.user.setCoinPathPosition(kingPath.size() * Constants.KING_PRICE);
        ArrayList<PoliticCard> politicCardArrayList = new ArrayList<>();
        ArrayList<Councilor> councilors = new ArrayList<>(this.game.getKing().getCouncil().getCouncil());

        for (Councilor councilor : councilors) {
            PoliticCard politicCard = new PoliticCard(councilor.getColor(), false);
            politicCardArrayList.add(politicCard);
            this.user.addPoliticCard(politicCard);
        }
        Action action = new MainActionBuildWithKingHelp(kingPath, politicCardArrayList);
        action.doAction(this.game, this.user);
    }


    // exception because of path
    @Test(expected = ActionNotPossibleException.class)
    public void testPathIncorrect() throws ActionNotPossibleException {

        ArrayList<City> kingPath = new ArrayList<>();
        kingPath.add(this.game.getKing().getCurrentCity());
        kingPath.add(this.game.getMap().getLinks().get(0).getCity2());

        ArrayList<PoliticCard> politicCardArrayList = new ArrayList<>();
        ArrayList<Councilor> councilors = new ArrayList<>(this.game.getKing().getCouncil().getCouncil());

        for (Councilor councilor : councilors) {
            PoliticCard politicCard = new PoliticCard(councilor.getColor(), false);
            politicCardArrayList.add(politicCard);
            this.user.addPoliticCard(politicCard);
        }
        Action action = new MainActionBuildWithKingHelp(kingPath, politicCardArrayList);
        action.doAction(this.game, this.user);
    }

    //test decrementing emporiums when present an emporium of another user
    @Test
    public void emporiumPresent() throws ActionNotPossibleException {
        int coin;

        int helper;
        ArrayList<City> kingPath = new ArrayList<>();
        kingPath.add(this.game.getKing().getCurrentCity());
        kingPath.add(this.game.getMap().getLinks().get(0).getCity2());

        this.userEnemy.addEmporium(this.game.getMap().getLinks().get(0).getCity2());

        this.user.setCoinPathPosition(kingPath.size() * Constants.KING_PRICE);
        this.user.setHelpers(10);
        helper = this.user.getHelpers().size();

        ArrayList<PoliticCard> politicCardArrayList = new ArrayList<>();
        ArrayList<Councilor> councilors = new ArrayList<>(this.game.getKing().getCouncil().getCouncil());

        for (Councilor councilor : councilors) {
            PoliticCard politicCard = new PoliticCard(councilor.getColor(), false);
            politicCardArrayList.add(politicCard);
            this.user.addPoliticCard(politicCard);
        }
        Action action = new MainActionBuildWithKingHelp(kingPath, politicCardArrayList);
        action.doAction(this.game, this.user);

        assertEquals(kingPath.get(kingPath.size() - 1), this.game.getKing().getCurrentCity());

        assertEquals(0, this.user.getPoliticCards().size());

        assertEquals(kingPath.get(kingPath.size() - 1), this.user.getUsersEmporium().get(0));

        assertEquals(helper - 1, this.user.getHelpers().size());

    }

    @Test(expected = ActionNotPossibleException.class)
    public void testNull() throws ActionNotPossibleException {
        int coin;

        int helper;
        ArrayList<City> kingPath = null;

        this.user.setHelpers(10);
        helper = this.user.getHelpers().size();

        ArrayList<PoliticCard> politicCardArrayList = new ArrayList<>();
        ArrayList<Councilor> councilors = new ArrayList<>(this.game.getKing().getCouncil().getCouncil());

        for (Councilor councilor : councilors) {
            PoliticCard politicCard = new PoliticCard(councilor.getColor(), false);
            politicCardArrayList.add(politicCard);
            this.user.addPoliticCard(politicCard);
        }
        Action action = new MainActionBuildWithKingHelp(kingPath, politicCardArrayList);
        action.doAction(this.game, this.user);

    }

}