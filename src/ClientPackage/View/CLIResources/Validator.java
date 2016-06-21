package ClientPackage.View.CLIResources;

import CommonModel.GameModel.City.City;
import CommonModel.GameModel.City.CityName;
import CommonModel.GameModel.City.RegionName;

import java.util.ArrayList;

/**
 * Created by Emanuele on 20/06/2016.
 */
public class Validator {


    public static boolean isValidCity(String city) {
        try {
            for(CityName cityName: CityName.values()){
                if(cityName.getCityName().equalsIgnoreCase(city)){
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static City getCity(String city, ArrayList<City> cities) {
        CityName cityName = null;
        for(CityName singleCityName:CityName.values()){
            if(singleCityName.getCityName().equalsIgnoreCase(city)){
                cityName=singleCityName;
            }
        }
        if(cityName!=null) {
            for (City singleCity : cities) {
                if (singleCity.getCityName().equals(cityName)) {
                    return singleCity;
                }
            }
        }
        return null;
    }

    public static boolean isValidRegion(String buyPermit) {
        try {
            RegionName regionName = RegionName.valueOf(buyPermit);
            return true;
        } catch (Exception e) {
            return false;
        }

    }
}
