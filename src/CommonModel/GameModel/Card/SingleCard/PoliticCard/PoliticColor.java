package CommonModel.GameModel.Card.SingleCard.PoliticCard;

import Utilities.Class.Constants;
import java.io.Serializable;

/**
 * Created by Giulio on 14/05/2016.
 */
public enum PoliticColor implements Serializable {

    VIOLET(Constants.VIOLET), BLACK(Constants.BLACK), ORANGE(Constants.ORANGE), PINK(Constants.PURPLE), BLUE(Constants.BLUE), WHITE(Constants.WHITE);

    private String color;

    PoliticColor() {
    }

    PoliticColor (String color){
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}
