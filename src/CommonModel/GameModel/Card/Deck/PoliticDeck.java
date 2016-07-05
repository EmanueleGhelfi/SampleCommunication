package CommonModel.GameModel.Card.Deck;

import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticCard;
import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticColor;
import Utilities.Class.Constants;

import java.io.Serializable;
import java.util.Collections;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by Emanuele on 14/05/2016.
 */
public class PoliticDeck implements Deck, Serializable {

    // stack for shuffle
    private final Stack<PoliticCard> politicDeckStack;
    private final Queue<PoliticCard> politicDeckQueue = new ArrayBlockingQueue<>(90);

    public PoliticDeck() {
        this.politicDeckStack = new Stack<>();
    }

    @Override
    public void createRandomDeck() {
        for (PoliticColor politicColor : PoliticColor.values()) {
            for (int cont = 0; cont < Constants.SINGLECOLOR_POLITIC_DECK_SIZE; cont++) {
                this.politicDeckStack.add(new PoliticCard(politicColor, false));
            }
        }
        // create multicolor
        for (int cont = 0; cont < Constants.MULTICOLOR_POLITIC_DECK_SIZE; cont++) {
            this.politicDeckStack.add(new PoliticCard(null, true));
        }
        Collections.shuffle(this.politicDeckStack);
        for (PoliticCard politicCard : this.politicDeckStack) {
            this.politicDeckQueue.add(politicCard);
        }
    }

    public PoliticCard drawACard() {
        return this.politicDeckQueue.remove();
    }

    public void addToQueue(Set<PoliticCard> politicCardSet) {
        for (PoliticCard politicCard : politicCardSet) {
            this.politicDeckQueue.add(politicCard);
        }
    }

    public int size() {
        return this.politicDeckQueue.size();
    }
}