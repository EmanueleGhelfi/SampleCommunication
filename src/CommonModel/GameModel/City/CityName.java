package CommonModel.GameModel.City;

import Utilities.Class.Constants;
import java.io.Serializable;

/**
 * Created by Giulio on 13/05/2016.
 */
public enum CityName implements Serializable {

    ARKON(Constants.ARKON), BURGEN(Constants.BURGEN),CASTRUM(Constants.CASTRUM), DORFUL(Constants.DORFUL), ESTI(Constants.ESTI), FRAMEK(Constants.FRAMEK), GRADEN(Constants.GRADEN), HELLAR(Constants.HELLAR),
    INDUR(Constants.INDUR), JUVELAR(Constants.JUVELAR), KULTOS(Constants.KULTOS), LYRAM(Constants.LYRAM), MERKATIM(Constants.MERKATIM), NARIS(Constants.NARIS), OSIUM(Constants.OSIUM);

    private String cityName;

    CityName() {
    }

    CityName (String cityName){
        this.cityName = cityName;
    }

    public String getCityName() {
        return cityName;
    }
}
