package CommonModel.GameModel;

import Server.UserClasses.User;

import java.util.ArrayList;

/**
 * Created by Giulio on 13/05/2016.
 */
public class City {

    private CityName cityName;
    private Color cityColor;
    private Region cityRegion;
    private ArrayList<City> closestCity;
    private ArrayList<User> userEmporiums; //TODO review emporiums of cities
    private Bonus bonus;

    public City(Color cityColor, CityName cityName, Region cityRegion) {
        this.cityColor = cityColor;
        this.cityName = cityName;
        this.cityRegion = cityRegion;
        userEmporiums =  new ArrayList<>();
        bonus = new MainBonus(1,2,5,false);
    }
}
