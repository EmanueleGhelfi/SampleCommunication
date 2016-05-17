package CommonModel.GameModel.Card.SingleCard.PoliticCard;

/**
 * Created by Giulio on 14/05/2016.
 */
public class PoliticCard {

    private PoliticColor politicColor;
    // true if card is multicolor
    private boolean isMultiColor;

    public PoliticCard(PoliticColor politicColor,boolean isMultiColor) {
        this.politicColor = politicColor;
        this.isMultiColor=isMultiColor;
    }

    public boolean isMultiColor() {
        return isMultiColor;
    }

    public PoliticColor getPoliticColor() {
        return politicColor;
    }

    @Override
    public String toString() {
        return "PoliticCard{" +
                "isMultiColor=" + isMultiColor +
                ", politicColor=" + politicColor +
                '}';
    }
}
