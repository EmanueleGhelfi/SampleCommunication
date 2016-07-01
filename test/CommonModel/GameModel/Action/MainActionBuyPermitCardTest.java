package CommonModel.GameModel.Action;

import CommonModel.GameModel.Bonus.Generic.MainBonus;
import CommonModel.GameModel.Card.SingleCard.PermitCard.PermitCard;
import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticCard;
import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticColor;
import CommonModel.GameModel.City.RegionName;
import Server.Controller.GamesManager;
import Server.Model.Game;
import Server.Model.User;
import Server.NetworkInterface.Communication.FakeCommunication;
import Server.NetworkInterface.Communication.RMICommunication;
import Utilities.Exception.ActionNotPossibleException;
import org.junit.Test;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by Emanuele on 01/07/2016.
 */
public class MainActionBuyPermitCardTest {


    @Test
    public void testBuy() throws Exception {
        /// TODO: 01/07/2016  
        Game game = new Game();
        User user = null;
        user = new User(new FakeCommunication(), GamesManager.getInstance());

        user.setCoinPathPosition(20);
        user.setMainActionCounter(2);
        ArrayList<PoliticCard> politicCardArrayList = new ArrayList<>();
        politicCardArrayList.add(new PoliticCard(PoliticColor.WHITE,false));
        politicCardArrayList.add(new PoliticCard(PoliticColor.WHITE,false));
        politicCardArrayList.add(new PoliticCard(PoliticColor.ORANGE,false));
        user.getPoliticCards().addAll(politicCardArrayList);
        RegionName region = RegionName.MOUNTAIN;
        PermitCard permitCard = new PermitCard(new MainBonus(1,5,9,false),null,region);
        MainActionBuyPermitCard mainActionBuyPermitCard = new MainActionBuyPermitCard(politicCardArrayList,region,permitCard);
        try {
            mainActionBuyPermitCard.doAction(game,user);
        } catch (ActionNotPossibleException e) {
            e.printStackTrace();
        }

    }
}