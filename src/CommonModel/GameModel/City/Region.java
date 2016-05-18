package CommonModel.GameModel.City;

import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticColor;
import CommonModel.GameModel.Council.Council;
import CommonModel.GameModel.Council.Councilor;
import Utilities.Class.Constants;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Giulio on 13/05/2016.
 */
public enum Region implements Serializable {

    MOUNTAIN (Constants.MOUNTAIN, Constants.REGION_BONUS), HILL (Constants.HILL, Constants.REGION_BONUS), COAST (Constants.COAST, Constants.REGION_BONUS);

    private final String region;
    private int cityNumber;
    private Council council;

    Region() {
        region = "";
    }

    private Region (String region, int cityNumber){
        this.region = region;
        this.cityNumber = cityNumber;
        council = new Council();
        council.add(new Councilor(PoliticColor.WHITE));
        council.add(new Councilor(PoliticColor.WHITE));
        council.add(new Councilor(PoliticColor.ORANGE));
        council.add(new Councilor(PoliticColor.BLUE));
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
    public Council getCouncil(){
        return council;
    }

}
