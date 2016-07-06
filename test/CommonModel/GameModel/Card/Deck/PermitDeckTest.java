package CommonModel.GameModel.Card.Deck;

import CommonModel.GameModel.Card.SingleCard.PermitCard.PermitCard;
import CommonModel.GameModel.City.RegionName;
import Server.Controller.GamesManager;
import Server.Model.Game;
import Server.Model.User;
import Server.NetworkInterface.Communication.FakeCommunication;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by Emanuele on 06/07/2016.
 */
public class PermitDeckTest {


    private PermitDeck permitDeck;
    private PermitDeck secondPermitDeck;

    @Before
    public void setUp() throws Exception {
        permitDeck = new PermitDeck(RegionName.HILL);
        secondPermitDeck = new PermitDeck(RegionName.HILL);


    }

    @Test
    public void testRandom() throws Exception {

        permitDeck.createRandomDeck();
        secondPermitDeck.createRandomDeck();
        assertTrue(!permitDeck.equals(secondPermitDeck));

    }

    @Test
    public void testChange() throws Exception {
        ArrayList<PermitCard> initArray = permitDeck.getVisibleArray();
        ArrayList<PermitCard> endArray= new ArrayList<>();

        permitDeck.changePermitCardVisibile();

        endArray = permitDeck.getVisibleArray();

        assertTrue(!initArray.equals(endArray));
    }


    @Test
    public void testGetAndRemove() throws Exception {
        permitDeck = new PermitDeck(RegionName.HILL);
        secondPermitDeck = new PermitDeck(RegionName.HILL);

        permitDeck.createRandomDeck();
        secondPermitDeck.createRandomDeck();

        PermitCard permitCard = permitDeck.getAndRemovePermitCardVisible(permitDeck.getVisibleArray().get(0));

        assertTrue(!permitDeck.getVisibleArray().contains(permitCard));
    }
}