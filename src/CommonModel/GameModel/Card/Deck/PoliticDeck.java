package CommonModel.GameModel.Card.Deck;

import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticCard;
import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticColor;
import Utilities.Class.Constants;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by Emanuele on 14/05/2016.
 */
public class PoliticDeck implements Deck,Serializable {

    // stack for shuffle
    private Stack<PoliticCard> politicDeckStack;
    private Queue<PoliticCard> politicDeckQueue = new ArrayBlockingQueue<>(90);

    public PoliticDeck() {
        politicDeckStack = new Stack<>();
    }

    @Override
    public void createRandomDeck() {
        for (PoliticColor politicColor: PoliticColor.values()) {
            for(int cont = 0; cont< Constants.SINGLECOLOR_POLITIC_DECK_SIZE; cont++){
                politicDeckStack.add(new PoliticCard(politicColor,false));
            }
        }
        // create multicolor
        for (int cont = 0; cont<Constants.MULTICOLOR_POLITIC_DECK_SIZE;cont++){
            politicDeckStack.add(new PoliticCard(null,true));
        }
        Collections.shuffle(politicDeckStack);
        for(PoliticCard politicCard: politicDeckStack){
            politicDeckQueue.add(politicCard);
        }
    }

    public PoliticCard drawACard(){
        return politicDeckQueue.remove();
    }

    public void addToQueue(Set<PoliticCard> politicCardSet){
        for (PoliticCard politicCard: politicCardSet){
            System.out.println(politicDeckQueue.offer(politicCard) + " SONO DENTRO ADDTOQUEUE");
            }
        }
}