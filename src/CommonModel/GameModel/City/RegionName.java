package CommonModel.GameModel.City;

import Utilities.Class.Constants;

/**
 * Created by Emanuele on 25/05/2016.
 */
public enum  RegionName {
    COAST (Constants.COAST),HILL (Constants.HILL), MOUNTAIN (Constants.MOUNTAIN);

    private String regionName;

    private RegionName (String region){
        this.regionName = region;

    }
}
