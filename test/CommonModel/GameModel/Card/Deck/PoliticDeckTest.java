package CommonModel.GameModel.Card.Deck;

import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticCard;
import Server.Controller.GamesManager;
import Server.Model.Game;
import Server.Model.User;
import Server.NetworkInterface.Communication.FakeCommunication;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by Emanuele on 02/07/2016.
 */
public class PoliticDeckTest {


    Game game;
    User user;

    @Before
    public void setUp() throws Exception {

        game = new Game();
        user = new User(new FakeCommunication(), GamesManager.getInstance());
    }

    @Test
    public void testAdd() throws Exception {

        int initSize;

        PoliticDeck politicDeck = game.getPoliticCards();

        initSize = politicDeck.size();

        ArrayList<PoliticCard> politicCards = new ArrayList<>();

        politicCards.add(politicDeck.drawACard());
        politicCards.add(politicDeck.drawACard());

        assertEquals(initSize-politicCards.size(),politicDeck.size());

        politicDeck.addToQueue(new HashSet<>(politicCards));

        assertEquals(initSize,politicDeck.size());

    }
}