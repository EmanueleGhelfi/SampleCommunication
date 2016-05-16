package CommonModel.GameModel;

import CommonModel.GameModel.Card.PoliticColor;

import java.io.Serializable;

/**
 * Created by Giulio on 14/05/2016.
 */
public class Councilor implements Serializable{

    private PoliticColor color;

    /**
     * Default constructor for deserialization
     */
    public Councilor() {
    }

    public Councilor(PoliticColor color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "Councilor{" +
                "color=" + color +
                '}';
    }
}
