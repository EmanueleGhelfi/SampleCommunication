package CommonModel.GameModel.Card.Deck;

import Utilities.Exception.ActionNotPossibleException;
import CommonModel.GameModel.Bonus.MainBonus;
import CommonModel.GameModel.City.CityFactory;
import CommonModel.GameModel.City.Region;
import CommonModel.GameModel.Card.PermitCard;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by Emanuele on 14/05/2016.
 */
public class PermitDeck implements Deck {

    private Stack<PermitCard> permitDeck;
    private Region region;
    private ArrayList<PermitCard> permitCardsVisible;

    public PermitDeck(Region region){
        permitDeck = new Stack<>();
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
            permitCardsVisible.add(permitDeck.pop());
            permitCardsVisible.add(permitDeck.pop());
        }
        else{
            System.out.println("Cities permit card null");
        }

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
