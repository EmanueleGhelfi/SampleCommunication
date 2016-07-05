package CommonModel.GameModel.Card.Deck;

import CommonModel.GameModel.Bonus.Generic.MainBonus;
import CommonModel.GameModel.Card.SingleCard.PermitCard.PermitCard;
import CommonModel.GameModel.City.CityFactory;
import CommonModel.GameModel.City.RegionName;
import Utilities.Class.Constants;
import Utilities.Exception.ActionNotPossibleException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by Emanuele on 14/05/2016.
 */
public class PermitDeck implements Deck, Serializable {

    private Queue<PermitCard> permitDeck;
    private RegionName region;
    private ArrayList<PermitCard> permitCardsVisible;

    public PermitDeck() {
    }

    public PermitDeck(RegionName region) {
        this.permitDeck = new ArrayBlockingQueue<>(Constants.REGION_DECK_SIZE);
        this.region = region;
    }

    @Override
    public void createRandomDeck() {
        //create a deck for this region
        ArrayList<ArrayList<Character>> citiesPermitCard = CityFactory.getCity(this.region);
        Collections.shuffle(citiesPermitCard);
        if (citiesPermitCard != null) {
            for (int i = 0; i < citiesPermitCard.size(); i++) {
                PermitCard permitCard = new PermitCard();
                permitCard.setRetroType(this.region);
                permitCard.setCityAcronimous(citiesPermitCard.get(i));
                permitCard.setBonus(new MainBonus(1, 3, 6, false));
                this.permitDeck.add(permitCard);
            }
            this.permitCardsVisible = new ArrayList<>();
            this.permitCardsVisible.add(this.permitDeck.remove());
            this.permitCardsVisible.add(this.permitDeck.remove());
        } else {
        }
    }

    public void changePermitCardVisibile() {
        for (PermitCard permitCard : this.permitCardsVisible) {
            this.permitDeck.add(permitCard);
        }
        this.permitCardsVisible.clear();
        this.permitCardsVisible.add(this.permitDeck.remove());
        this.permitCardsVisible.add(this.permitDeck.remove());
    }

    public PermitCard getAndRemoveRandomPermitCard() {
        return this.permitDeck.remove();
    }

    public PermitCard getAndRemovePermitCardVisible(PermitCard permitCard) throws ActionNotPossibleException {
        if (this.permitCardsVisible.contains(permitCard)) {
            for (int i = 0; i < this.permitCardsVisible.size(); i++) {
                if (this.permitCardsVisible.get(i).equals(permitCard)) {
                    PermitCard permitCardToReturn = this.permitCardsVisible.remove(i);
                    this.permitCardsVisible.add(this.permitDeck.remove());
                    return permitCardToReturn;
                }
            }
        }
        throw new ActionNotPossibleException(Constants.PERMIT_CARD_NOT_PRESENT_EXCEPTION);
    }

    public PermitCard getPermitCardVisible(int num) {
        if (num >= 2 || num < 0) {
            return null;
        } else {
            return this.permitCardsVisible.get(num);
        }
    }

    /**
     * @return the visible permit card of this.region
     */
    public ArrayList<PermitCard> getVisibleArray() {
        return (ArrayList<PermitCard>) this.permitCardsVisible.clone();
    }

}
