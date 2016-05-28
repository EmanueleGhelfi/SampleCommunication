package CommonModel.GameModel.Card.Deck;

import CommonModel.GameModel.City.RegionName;
import Utilities.Class.Constants;
import Utilities.Exception.ActionNotPossibleException;
import CommonModel.GameModel.Bonus.Generic.MainBonus;
import CommonModel.GameModel.City.CityFactory;
import CommonModel.GameModel.City.Region;
import CommonModel.GameModel.Card.SingleCard.PermitCard.PermitCard;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by Emanuele on 14/05/2016.
 */
public class PermitDeck implements Deck,Serializable {

    private Queue<PermitCard> permitDeck;
    private RegionName region;
    private ArrayList<PermitCard> permitCardsVisible;

    public PermitDeck() {
    }

    public PermitDeck(RegionName region){
        permitDeck = new ArrayBlockingQueue<>(Constants.REGION_DECK_SIZE);
        this.region = region;
    }

    @Override
    public void createRandomDeck() {
        //create a deck for this region
        System.out.println("creating deck of: "+region);
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

    public PermitCard getAndRemovePermitCardVisible(PermitCard permitCard) throws ActionNotPossibleException{
        if(permitCardsVisible.contains(permitCard)) {
            for (int i = 0; i < permitCardsVisible.size(); i++) {
                if(permitCardsVisible.get(i).equals(permitCard)){
                    PermitCard permitCardToReturn = permitCardsVisible.remove(i);
                    System.out.println("Changing permit card visible");
                    permitCardsVisible.add(permitDeck.remove());
                    return permitCardToReturn;
                }
            }
        }
        throw new ActionNotPossibleException();
    }

    public PermitCard getPermitCardVisible(int num){
        if(num>=2 || num<0){
            return null;
        }
        else{
            return permitCardsVisible.get(num);
        }
    }

    public ArrayList<PermitCard> getVisibleArray(){
        return permitCardsVisible;
    }
}
