package CommonModel.GameModel;

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
        for(int i = 0; i<15;i++){
            PermitCard permitCard = new PermitCard();

            permitCard.setBonus();
        }


    }
}
