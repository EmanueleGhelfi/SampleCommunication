package CommonModel.GameModel.Card.SingleCard.PoliticCard;

import Utilities.Class.Constants;
import java.io.Serializable;

/**
 * Created by Giulio on 14/05/2016.
 */
public enum PoliticColor implements Serializable, Cloneable {

    VIOLET(Constants.VIOLET), BLACK(Constants.BLACK), ORANGE(Constants.ORANGE), PURPLE(Constants.PURPLE), BLUE(Constants.BLUE), WHITE(Constants.WHITE);

    private String color;
    private String imageUrl;

    PoliticColor() {
    }

    PoliticColor (String color){
        this.color = color;
        this.imageUrl="/ClientPackage/View/GUIResources/Image/councilor/"+color+".png";
    }

    public String getColor() {
        return color;
    }

    public String getImageUrl() {
        return imageUrl;
    }


}
