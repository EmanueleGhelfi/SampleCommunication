package CommonModel.GameModel.Card.Deck;

import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticCard;
import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticColor;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by Emanuele on 14/05/2016.
 */
public class PoliticDeck implements Deck {

    private Stack<PoliticCard> politicDeckStack;
    private Queue<PoliticCard> politicDeckQueue = new ArrayBlockingQueue<>(90);

    public PoliticDeck() {
        politicDeckStack = new Stack<>();
    }

    @Override
    public void createRandomDeck() {

        for (PoliticColor politicColor: PoliticColor.values()) {

            for(int i = 0;i<13;i++){
                politicDeckStack.add(new PoliticCard(politicColor,false));
            }

        }
        // create multicolor
        for (int i = 0; i<12;i++){
            politicDeckStack.add(new PoliticCard(null,true));
        }
        Collections.shuffle(politicDeckStack);
        for(PoliticCard politicCard: politicDeckStack){
            politicDeckQueue.add(politicCard);
        }
    }

    public void printDeck(){
        for (PoliticCard politicCard: politicDeckStack) {
            System.out.println(politicCard);
        }
    }

    public PoliticCard drawACard(){
        return politicDeckQueue.remove();
    }

    public static void main(String[] args){
        PoliticDeck politicDeck = new PoliticDeck();
        politicDeck.createRandomDeck();
        politicDeck.printDeck();
    }

    public void addToQueue(Set<PoliticCard> politicCardSet){
        for (PoliticCard politicCard: politicCardSet){
            System.out.println(politicDeckQueue.offer(politicCard) + " SONO DENTRO ADDTOQUEUE");
            }
        }
}