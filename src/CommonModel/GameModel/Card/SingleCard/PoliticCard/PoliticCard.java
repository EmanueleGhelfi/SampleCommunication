package CommonModel.GameModel.Card.SingleCard.PoliticCard;

import CommonModel.GameModel.Market.BuyableObject;
import Utilities.Class.Constants;

import java.io.Serializable;

/**
 * Created by Giulio on 14/05/2016.
 */
public class PoliticCard implements Serializable,BuyableObject {

    private PoliticColor politicColor;
    // true if card is multicolor
    private boolean isMultiColor;

    public PoliticCard() {
    }

    public PoliticCard(PoliticColor politicColor, boolean isMultiColor) {
        this.politicColor = politicColor;
        this.isMultiColor=isMultiColor;
    }

    @Override
    public String toString() {
        return "PoliticCard{" +
                "isMultiColor=" + isMultiColor +
                ", politicColor=" + politicColor +
                '}';
    }

    public boolean isMultiColor() {
        return isMultiColor;
    }
    public PoliticColor getPoliticColor() {
        return politicColor;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PoliticCard that = (PoliticCard) o;

        if (isMultiColor != that.isMultiColor) return false;
        return politicColor == that.politicColor;

    }

    @Override
    public int hashCode() {
        int result = politicColor != null ? politicColor.hashCode() : 0;
        result = 31 * result + (isMultiColor ? 1 : 0);
        return result;
    }

    @Override
    public String getType() {
        return Constants.POLITIC_CARD;
    }

    @Override
    public String getInfo() {
        return this.toString();
    }
}
