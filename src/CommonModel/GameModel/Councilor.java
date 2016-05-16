package CommonModel.GameModel;

import CommonModel.GameModel.Card.PoliticColor;

/**
 * Created by Giulio on 14/05/2016.
 */
public class Councilor {

    private PoliticColor color;

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
