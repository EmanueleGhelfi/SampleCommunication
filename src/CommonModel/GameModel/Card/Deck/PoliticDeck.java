package CommonModel.GameModel.Card.Deck;



import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticCard;
import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticColor;

import java.util.Collections;
import java.util.Stack;

/**
 * Created by Emanuele on 14/05/2016.
 */
public class PoliticDeck implements Deck {

    private Stack<PoliticCard> politicDeck;

    public PoliticDeck() {
        politicDeck = new Stack<>();
    }

    @Override
    public void createRandomDeck() {

        for (PoliticColor politicColor: PoliticColor.values()) {

            for(int i = 0;i<13;i++){
                politicDeck.add(new PoliticCard(politicColor,false));
            }

        }

        // create multicolor
        for (int i = 0; i<12;i++){
            politicDeck.add(new PoliticCard(null,true));
        }

        Collections.shuffle(politicDeck);

    }

    public void printDeck(){
        for (PoliticCard politicCard: politicDeck) {
            System.out.println(politicCard);

        }
    }

    public static void main(String[] args){
        PoliticDeck politicDeck = new PoliticDeck();
        politicDeck.createRandomDeck();
        politicDeck.printDeck();


    }
}
