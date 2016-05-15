package CommonModel.GameModel;

import Server.UserClasses.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Emanuele on 15/05/2016.
 */
public class CityVisitor{

    private City initialCity;
    private User user;
    private ArrayList<City> nearestCity;
    private HashMap<City,Boolean> allCity;

    public CityVisitor(City initialCity, User user,ArrayList<City> allCity) {
        this.initialCity = initialCity;
        this.user = user;
        for (City city : allCity) {
            this.allCity.put(city,false);

        }
    }

    public ArrayList<City> visit(){
        ArrayList<City> arrayToReturn = new ArrayList<>();
        // TODO: visit al cities

    }
}
