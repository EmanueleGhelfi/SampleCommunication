package CommonModel.GameModel;

/**
 * Created by Giulio on 13/05/2016.
 */
public enum Region {

    MOUNTAIN ("mountain", 5), HILL ("hill", 5), COAST ("coast", 5);

    private final String region;
    private int cityNumber;
    private Council council;

    private Region (String region, int cityNumber){
        this.region = region;
        this.cityNumber = cityNumber;
    }

    public String getRegion() {
        return region;
    }

    public void setCouncil(Council council){
        this.council = council;
    }

}
