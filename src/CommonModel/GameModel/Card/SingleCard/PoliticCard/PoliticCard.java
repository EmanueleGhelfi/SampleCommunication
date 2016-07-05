package CommonModel.GameModel.Card.SingleCard.PoliticCard;

import CommonModel.GameModel.Market.BuyableObject;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Giulio on 14/05/2016.
 */
public class PoliticCard implements BuyableObject {

    // for equals, id of the object
    static final AtomicLong NEXT_ID = new AtomicLong(0);
    final long id = PoliticCard.NEXT_ID.getAndIncrement();
    private PoliticColor politicColor;
    // true if card is multicolor
    private boolean isMultiColor;

    public PoliticCard() {
    }

    public PoliticCard(PoliticColor politicColor, boolean isMultiColor) {
        this.politicColor = politicColor;
        this.isMultiColor = isMultiColor;
    }

    @Override
    public String toString() {
        return "PoliticCard{" +
                "isMultiColor=" + this.isMultiColor +
                ", politicColor=" + this.politicColor +
                '}';
    }

    public boolean isMultiColor() {
        return this.isMultiColor;
    }

    public PoliticColor getPoliticColor() {
        return this.politicColor;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;

        PoliticCard that = (PoliticCard) o;

        if (this.isMultiColor != that.isMultiColor) return false;
        if (this.id != that.id) return false;
        return this.politicColor == that.politicColor;

    }

    @Override
    public int hashCode() {
        int result = this.politicColor != null ? this.politicColor.hashCode() : 0;
        result = 31 * result + (this.isMultiColor ? 1 : 0);
        return result;
    }


    @Override
    public String getInfo() {
        if (this.isMultiColor()) {
            return "Multicolor";
        } else {
            return this.getPoliticColor().getColor();
        }
    }

    @Override
    public BuyableObject getCopy() {
        try {
            return (BuyableObject) clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getUrl() {
        if (this.isMultiColor())
            return "MulticolorPoliticCard";
        return this.getPoliticColor().getColor().substring(0, 1).toUpperCase() + this.getPoliticColor().getColor().substring(1) + "PoliticCard";
    }
}
