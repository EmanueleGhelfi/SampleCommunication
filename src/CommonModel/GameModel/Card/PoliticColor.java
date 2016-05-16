package CommonModel.GameModel.Card;

import java.io.Serializable;

/**
 * Created by Giulio on 14/05/2016.
 */
public enum PoliticColor implements Serializable {

    VIOLET("violet"), BLACK("black"), ORANGE("orange"), PINK("pink"), BLUE("blue"), WHITE("white");

    private String color;

    private PoliticColor (String color){
        this.color = color;
    }

}
