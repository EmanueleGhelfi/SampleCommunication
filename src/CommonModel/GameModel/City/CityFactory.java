package CommonModel.GameModel.City;

import CommonModel.GameModel.Card.SingleCard.PermitCard.PermitCardCoastCity;
import CommonModel.GameModel.Card.SingleCard.PermitCard.PermitCardHillCity;
import CommonModel.GameModel.Card.SingleCard.PermitCard.PermitCardMountainCity;
import java.util.ArrayList;

/**
 * Created by Giulio on 15/05/2016.
 */
public class CityFactory {

    public static ArrayList<ArrayList<Character>> getCity(Region region){
        if (region.equals(Region.MOUNTAIN))
            return PermitCardMountainCity.getCities();
        if (region.equals(Region.COAST))
            return PermitCardCoastCity.getCities();
        if (region.equals(Region.HILL))
            return PermitCardHillCity.getCities();
        return null;
    }

}
