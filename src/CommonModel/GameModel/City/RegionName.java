package CommonModel.GameModel.City;

import Utilities.Class.Constants;

/**
 * Created by Emanuele on 25/05/2016.
 */
public enum  RegionName {
    MOUNTAIN (Constants.MOUNTAIN), HILL (Constants.HILL), COAST (Constants.COAST);

    private String regionName;

    private RegionName (String region){
        this.regionName = region;

    }
}
