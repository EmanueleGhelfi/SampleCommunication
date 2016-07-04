package CommonModel.GameModel.Card.SingleCard.PoliticCard;

import CommonModel.GameModel.Market.BuyableObject;
import Utilities.Class.Constants;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Giulio on 14/05/2016.
 */
public class PoliticCard implements Serializable,BuyableObject {

    private PoliticColor politicColor;

    // true if card is multicolor
    private boolean isMultiColor;

    // for equals, id of the object
    static final AtomicLong NEXT_ID = new AtomicLong(0);
    final long id = NEXT_ID.getAndIncrement();

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
        if (id != that.id) return false;
        return politicColor == that.politicColor;

    }

    @Override
    public int hashCode() {
        int result = politicColor != null ? politicColor.hashCode() : 0;
        result = 31 * result + (isMultiColor ? 1 : 0);
        return result;
    }



    @Override
    public String getInfo() {
        if(isMultiColor()){
            return "Multicolor";
        }
        else {
            return getPoliticColor().getColor();
        }
    }

    @Override
    public BuyableObject getCopy() {
        try {
            return (BuyableObject) this.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getUrl() {
        if (isMultiColor())
            return "MulticolorPoliticCard";
        return getPoliticColor().getColor().substring(0, 1).toUpperCase() + getPoliticColor().getColor().substring(1) + "PoliticCard";
    }
}
