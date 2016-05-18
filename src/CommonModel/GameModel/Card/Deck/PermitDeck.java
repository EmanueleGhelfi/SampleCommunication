package CommonModel.GameModel.Card.Deck;

import Utilities.Class.Constants;
import Utilities.Exception.ActionNotPossibleException;
import CommonModel.GameModel.Bonus.Generic.MainBonus;
import CommonModel.GameModel.City.CityFactory;
import CommonModel.GameModel.City.Region;
import CommonModel.GameModel.Card.SingleCard.PermitCard.PermitCard;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by Emanuele on 14/05/2016.
 */
public class PermitDeck implements Deck {

    private Queue<PermitCard> permitDeck;
    private Region region;
    private ArrayList<PermitCard> permitCardsVisible;

    public PermitDeck(Region region){
        permitDeck = new ArrayBlockingQueue<>(Constants.FIFTHEEN);
        this.region = region;
    }

    @Override
    public void createRandomDeck() {
        //create a deck for this region
        ArrayList<ArrayList<Character>> citiesPermitCard = CityFactory.getCity(region);
        if(citiesPermitCard!=null) {
            for (int i = 0; i < citiesPermitCard.size(); i++) {
                PermitCard permitCard = new PermitCard();
                permitCard.setRetroType(region);
                permitCard.setCityAcronimous(citiesPermitCard.get(i));
                permitCard.setBonus(new MainBonus(1, 3, 6, false));
                permitDeck.add(permitCard);
            }
            permitCardsVisible = new ArrayList<>();
            permitCardsVisible.add(permitDeck.remove());
            permitCardsVisible.add(permitDeck.remove());
        }
        else{
            System.out.println("Cities permit card null");
        }
    }

    public void changePermitCardVisibile (){
        for (PermitCard permitCard: permitCardsVisible){
            permitDeck.add(permitCard);
        }
        permitCardsVisible.clear();
        permitCardsVisible.add(permitDeck.remove());
        permitCardsVisible.add(permitDeck.remove());
    }

    public PermitCard getPermitCardVisible(PermitCard permitCard) throws ActionNotPossibleException{
        if(permitCardsVisible.contains(permitCard)){
            for (PermitCard permitCardToReturn: permitCardsVisible) {
                if(permitCardToReturn.equals(permitCard))
                    return permitCardToReturn;

            }
        }
        throw new ActionNotPossibleException();
    }

    public static void main(String[] args){
        System.out.println("Start test deck");
        PermitDeck permitDeckMountain = new PermitDeck(Region.MOUNTAIN);
        permitDeckMountain.createRandomDeck();
    }
}
