package CommonModel.GameModel.Card.SingleCard.PoliticCard;

import Utilities.Class.Constants;

import java.io.Serializable;

/**
 * Created by Giulio on 14/05/2016.
 */
public enum PoliticColor implements Serializable {

    VIOLET(Constants.VIOLET), BLACK(Constants.BLACK), ORANGE(Constants.ORANGE), PINK(Constants.PINK), BLUE(Constants.BLUE), WHITE(Constants.WHITE);

    private String color;

    PoliticColor() {
    }

    private PoliticColor (String color){
        this.color = color;
    }

}
