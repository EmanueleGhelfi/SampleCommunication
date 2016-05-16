package CommonModel.GameModel.Card.Deck;

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

    public PermitDeck(Region region){
        permitDeck = new Stack<>();
        this.region = region;
    }

    @Override
    public void createRandomDeck() {
        //create a deck for this region
        ArrayList<ArrayList<Character>> citiesPermitCard = CityFactory.getCity(region);
        for(int i = 0; i<15;i++){
            PermitCard permitCard = new PermitCard();
            permitCard.setRetroType(region);
            permitCard.setCityAcronimous(citiesPermitCard.get(i));
            permitCard.setBonus(new MainBonus(1,3,6,false));
        }


    }

    public static void main(String[] args){
        System.out.println("Start test deck");
        PermitDeck permitDeckMountain = new PermitDeck(Region.MOUNTAIN);
        permitDeckMountain.createRandomDeck();



    }
}
