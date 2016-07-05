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

        game = new Game();
        user = new User(new FakeCommunication(), GamesManager.getInstance());
        user.setUsername("User");

        userEnemy = new User(new FakeCommunication(), GamesManager.getInstance());
        userEnemy.setGame(game);
        userEnemy.setUsername("UserEnemy");
        user.setGame(game);
        user.setFastActionCounter(4);
        user.setMainActionCounter(4);

        user.setNobilityPathPosition(game.getNobilityPath().getPosition()[Constants.INITIAL_POSITION_ON_NOBILITY_PATH]);

        Map map = Map.readAllMap().get(0);
        game.setMap(map);

        King king = new King(game.getMap().getLinks().get(0).getCity1(), game.getBank());
        game.setKing(king);

        game.addUserToGame(user);
        game.addUserToGame(userEnemy);
    }


    // king goes in correct city
    @Test(timeout = 200)
    public void testBuild() throws Exception {
        int coin;

        ArrayList<City> kingPath = new ArrayList<>();
        kingPath.add(game.getKing().getCurrentCity());
        kingPath.add(game.getMap().getLinks().get(0).getCity2());
        user.setCoinPathPosition(kingPath.size() * Constants.KING_PRICE);
        ArrayList<PoliticCard> politicCardArrayList = new ArrayList<>();
        ArrayList<Councilor> councilors = new ArrayList<>(game.getKing().getCouncil().getCouncil());

        for (Councilor councilor : councilors) {
            PoliticCard politicCard = new PoliticCard(councilor.getColor(), false);
            politicCardArrayList.add(politicCard);
            user.addPoliticCard(politicCard);
        }
        Action action = new MainActionBuildWithKingHelp(kingPath, politicCardArrayList);
        action.doAction(game, user);

        assertEquals(kingPath.get(kingPath.size() - 1), game.getKing().getCurrentCity());

        assertEquals(0, user.getPoliticCards().size());

        assertEquals(kingPath.get(kingPath.size() - 1), user.getUsersEmporium().get(0));
    }

    //action not possible because of coin
    @Test(expected = ActionNotPossibleException.class)
    public void testActionNotPossible() throws ActionNotPossibleException {
        user.setCoinPathPosition(0);

        ArrayList<City> kingPath = new ArrayList<>();
        kingPath.add(game.getKing().getCurrentCity());
        kingPath.add(game.getMap().getLinks().get(7).getCity2());
        user.setCoinPathPosition(kingPath.size() * Constants.KING_PRICE);
        ArrayList<PoliticCard> politicCardArrayList = new ArrayList<>();
        ArrayList<Councilor> councilors = new ArrayList<>(game.getKing().getCouncil().getCouncil());

        for (Councilor councilor : councilors) {
            PoliticCard politicCard = new PoliticCard(councilor.getColor(), false);
            politicCardArrayList.add(politicCard);
            user.addPoliticCard(politicCard);
        }
        Action action = new MainActionBuildWithKingHelp(kingPath, politicCardArrayList);
        action.doAction(game, user);
    }


    // exception because of path
    @Test(expected = ActionNotPossibleException.class)
    public void testPathIncorrect() throws ActionNotPossibleException {

        ArrayList<City> kingPath = new ArrayList<>();
        kingPath.add(game.getKing().getCurrentCity());
        kingPath.add(game.getMap().getLinks().get(0).getCity2());

        ArrayList<PoliticCard> politicCardArrayList = new ArrayList<>();
        ArrayList<Councilor> councilors = new ArrayList<>(game.getKing().getCouncil().getCouncil());

        for (Councilor councilor : councilors) {
            PoliticCard politicCard = new PoliticCard(councilor.getColor(), false);
            politicCardArrayList.add(politicCard);
            user.addPoliticCard(politicCard);
        }
        Action action = new MainActionBuildWithKingHelp(kingPath, politicCardArrayList);
        action.doAction(game, user);
    }

    //test decrementing emporiums when present an emporium of another user
    @Test
    public void emporiumPresent() throws ActionNotPossibleException {
        int coin;

        int helper;
        ArrayList<City> kingPath = new ArrayList<>();
        kingPath.add(game.getKing().getCurrentCity());
        kingPath.add(game.getMap().getLinks().get(0).getCity2());

        userEnemy.addEmporium(game.getMap().getLinks().get(0).getCity2());

        user.setCoinPathPosition(kingPath.size() * Constants.KING_PRICE);
        user.setHelpers(10);
        helper = user.getHelpers().size();

        ArrayList<PoliticCard> politicCardArrayList = new ArrayList<>();
        ArrayList<Councilor> councilors = new ArrayList<>(game.getKing().getCouncil().getCouncil());

        for (Councilor councilor : councilors) {
            PoliticCard politicCard = new PoliticCard(councilor.getColor(), false);
            politicCardArrayList.add(politicCard);
            user.addPoliticCard(politicCard);
        }
        Action action = new MainActionBuildWithKingHelp(kingPath, politicCardArrayList);
        action.doAction(game, user);

        assertEquals(kingPath.get(kingPath.size() - 1), game.getKing().getCurrentCity());

        assertEquals(0, user.getPoliticCards().size());

        assertEquals(kingPath.get(kingPath.size() - 1), user.getUsersEmporium().get(0));

        assertEquals(helper - 1, user.getHelpers().size());

    }

    @Test(expected = ActionNotPossibleException.class)
    public void testNull() throws ActionNotPossibleException {
        int coin;

        int helper;
        ArrayList<City> kingPath = null;

        user.setHelpers(10);
        helper = user.getHelpers().size();

        ArrayList<PoliticCard> politicCardArrayList = new ArrayList<>();
        ArrayList<Councilor> councilors = new ArrayList<>(game.getKing().getCouncil().getCouncil());

        for (Councilor councilor : councilors) {
            PoliticCard politicCard = new PoliticCard(councilor.getColor(), false);
            politicCardArrayList.add(politicCard);
            user.addPoliticCard(politicCard);
        }
        Action action = new MainActionBuildWithKingHelp(kingPath, politicCardArrayList);
        action.doAction(game, user);

    }

}