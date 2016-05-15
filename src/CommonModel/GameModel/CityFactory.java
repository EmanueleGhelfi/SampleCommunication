package CommonModel.GameModel;

/**
 * Created by Giulio on 15/05/2016.
 */
public class CityFactory {

    public static CityEnum[] getCity(Region region){
        if (region.equals(Region.MOUNTAIN))
            return MountainCityPermitCard.getCities();
        if (region.equals(Region.COAST))
            return CoastCityPermitCard.getCities();
        if (region.equals(Region.HILL))
            return HillCityPermitCard.getCities();
        return null;
    }

}
