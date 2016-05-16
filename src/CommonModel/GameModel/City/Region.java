package CommonModel.GameModel.City;

import CommonModel.GameModel.Council;
import Server.Model.Game;

import java.util.ArrayList;

/**
 * Created by Giulio on 13/05/2016.
 */
public enum Region {

    MOUNTAIN ("mountain", 5), HILL ("hill", 5), COAST ("coast", 5);

    private final String region;
    private int cityNumber;
    private Council council;

    private Region (String region, int cityNumber){
        this.region = region;
        this.cityNumber = cityNumber;
    }

    public boolean checkRegion(ArrayList<City> userEmporiums){
        int cityCounter = 0;
        for (City city: userEmporiums) {
            if (city.getRegion() == this) {
                cityCounter++;
            }
        }
        if (cityCounter == cityNumber){
            return true;
        }
        return false;
    }

    public String getRegion() {
        return region;
    }

    public void setCouncil(Council council){
        this.council = council;
    }

    public Council getCouncil(){
        return council;
    }

}
