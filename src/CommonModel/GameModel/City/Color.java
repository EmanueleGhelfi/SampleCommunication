package CommonModel.GameModel.City;

import Utilities.Class.Constants;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Giulio on 13/05/2016.
 */
public enum Color {

    BLUE(Constants.BLUE, Constants.BLUE_COUNTER), GREY(Constants.GREY, Constants.GREY_COUNTER),
    ORANGE(Constants.ORANGE, Constants.ORANGE_COUNTER), YELLOW(Constants.YELLOW, Constants.YELLOW_COUNTER),
    PURPLE(Constants.PURPLE, Constants.PURPLE_COUNTER);

    private String color;
    private int cityNumber;

    Color() {
    }

    Color(String color, int cityNumber) {
        this.color = color;
        this.cityNumber = cityNumber;
    }

    public boolean checkColor(ArrayList<City> userEmporiums) {
        int cityCounter = 0;
        for (City city : userEmporiums) {
            if (city.getColor() == this) {
                cityCounter++;
            }
        }
        return cityCounter == this.cityNumber;
    }

    public String getColor() {
        return this.color;
    }

}
