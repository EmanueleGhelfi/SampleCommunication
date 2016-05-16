package CommonModel.GameModel.City;

import CommonModel.GameModel.Card.CoastCityPermitCard;
import CommonModel.GameModel.Card.HillCityPermitCard;
import CommonModel.GameModel.Card.MountainCityPermitCard;

import java.util.ArrayList;

/**
 * Created by Giulio on 15/05/2016.
 */
public class CityFactory {

    public static ArrayList<ArrayList<Character>> getCity(Region region){
        if (region.equals(Region.MOUNTAIN))
            return MountainCityPermitCard.getCities();
        if (region.equals(Region.COAST))
            return CoastCityPermitCard.getCities();
        if (region.equals(Region.HILL))
            return HillCityPermitCard.getCities();
        return null;
    }

}
