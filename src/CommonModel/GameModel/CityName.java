package CommonModel.GameModel;

/**
 * Created by Giulio on 13/05/2016.
 */
public enum CityName {

    ARKON("Arkon"), BURGEN("Burgen"),CASTRUM("Castrum"), DORFUL("Dorful"), ESTI("Esti"), FRAMEK("Framek"), GRADEN("Graden"), HELLAR("Hellar"),
    INDUR("Indur"), JUVELAR("Juvelar"), KULTOS("Kultos"), LYRAM("Lyram"), MERKATIM("Merkatim"), NARIS("Naris"), OSIUM("Osium");

    private String cityName;

    private CityName (String cityName){
        this.cityName = cityName;
    }

    public String getCityName() {
        return cityName;
    }
}
