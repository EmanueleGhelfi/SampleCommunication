package ClientPackage.View.CLIResources;

import CommonModel.GameModel.City.City;
import CommonModel.GameModel.City.CityName;

import java.util.ArrayList;

/**
 * Created by Emanuele on 20/06/2016.
 */
public class Validator {


    public static boolean isValidCity(String city) {
        try {
            CityName cityName = CityName.valueOf(city);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static City getCity(String city, ArrayList<City> cities) {
        CityName cityName = CityName.valueOf(city);
        for(City singleCity: cities){
            if(singleCity.getCityName().equals(cityName)){
                return singleCity;
            }
        }
        return null;
    }
}
