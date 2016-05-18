package CommonModel.GameModel.Card.SingleCard.PoliticCard;

import java.io.Serializable;

/**
 * Created by Giulio on 14/05/2016.
 */
public class PoliticCard implements Serializable {

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
}
