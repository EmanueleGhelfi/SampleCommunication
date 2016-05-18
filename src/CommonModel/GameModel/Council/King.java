package CommonModel.GameModel.Council;

import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticColor;
import CommonModel.GameModel.City.City;
import CommonModel.GameModel.City.CityName;
import CommonModel.GameModel.City.Color;
import CommonModel.GameModel.City.Region;
import java.io.Serializable;

/**
 * Created by Giulio on 14/05/2016.
 */
public class King implements Serializable {

    private City currentCity;
    private Council council;

    public King() {
        this.council = new Council();
        council.add(new Councilor(PoliticColor.BLACK));
        council.add(new Councilor(PoliticColor.ORANGE));
        council.add(new Councilor(PoliticColor.PINK));
        council.add(new Councilor(PoliticColor.WHITE));
        this.currentCity = new City(Color.BLUE, CityName.ARKON, Region.COAST);
    }

    public King(City currentCity) {
        this.council = new Council();
        council.add(new Councilor(PoliticColor.BLACK));
        council.add(new Councilor(PoliticColor.ORANGE));
        council.add(new Councilor(PoliticColor.PINK));
        council.add(new Councilor(PoliticColor.WHITE));
        this.currentCity = currentCity;
    }

    public Council getCouncil() {
        return council;
    }
}