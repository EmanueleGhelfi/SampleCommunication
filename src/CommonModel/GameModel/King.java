package CommonModel.GameModel;

import CommonModel.GameModel.City.City;

/**
 * Created by Giulio on 14/05/2016.
 */
public class King {

    private City currentCity;
    private Council council;

    public void setCouncil(Council council) {
        this.council = council;
    }

    public void setCurrentCity(City currentCity) {
        this.currentCity = currentCity;
    }

    public Council getCouncil() {
        return council;
    }

    public City getCurrentCity() {
        return currentCity;
    }
}