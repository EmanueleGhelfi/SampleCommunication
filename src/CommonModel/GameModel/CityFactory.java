package CommonModel.GameModel;

/**
 * Created by Giulio on 15/05/2016.
 */
public class CityFactory {

    public static CityEnum getCity(Region region){
        if (region.equals(Region.MOUNTAIN))
            return new MountainCityPermitCard();
        if (region.equals())
            return new
    }

}
