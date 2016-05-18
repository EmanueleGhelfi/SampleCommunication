package CommonModel.GameModel.City;

import Server.Model.Game;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Giulio on 13/05/2016.
 */
public enum Color implements Serializable {

    BLUE("blue", 2), GREY ("grey", 4), ORANGE ("orange", 3), YELLOW ("yellow", 5), PURPLE ("purple", 1);

    private String color;
    private int cityNumber;

    Color() {
    }

    private Color (String color, int cityNumber){
        this.color = color;
        this.cityNumber = cityNumber;
    }

    public boolean checkColor(ArrayList<City> userEmporiums){
        int cityCounter = 0;
        for (City city: userEmporiums) {
            if (city.getColor() == this) {
                cityCounter++;
            }
        }
        if (cityCounter == cityNumber){
            return true;
        }
        return false;
    }

    public String getColor(){
        return color;
    }

    public int getCityNumber(){
        return cityNumber;
    }

}
