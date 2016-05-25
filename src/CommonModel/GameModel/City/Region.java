package CommonModel.GameModel.City;

import CommonModel.GameModel.Action.Action;
import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticCard;
import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticColor;
import CommonModel.GameModel.Council.Council;
import CommonModel.GameModel.Council.Councilor;
import CommonModel.GameModel.Council.GotCouncil;
import Utilities.Class.Constants;
import Utilities.Class.InterfaceAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Giulio on 13/05/2016.
 */
public enum Region implements Serializable, GotCouncil {

    MOUNTAIN (Constants.MOUNTAIN, Constants.REGION_BONUS), HILL (Constants.HILL, Constants.REGION_BONUS), COAST (Constants.COAST, Constants.REGION_BONUS);

    private final String region;
    private int cityNumber;
    private Council council;

    //default
    Region() {
        region = "";
    }

    private Region (String region, int cityNumber){
        this.region = region;
        this.cityNumber = cityNumber;
    }

    public void createRandomCouncil(){
        if(council==null) {
            council = new Council();
            Random random = new Random();
            for (int i = 0; i < Constants.COUNCILOR_DIMENSION; i++) {
                PoliticColor[] politicColors = PoliticColor.values();
                int value = random.nextInt(5);
                System.out.println("Random color in region called " + politicColors[value]);
                council.add(new Councilor(politicColors[value]));
            }
        }
    }

    public boolean checkRegion(ArrayList<City> userEmporiums){
        int cityCounter = 0;
        for (City city: userEmporiums) {
            if (city.getRegion() == this) {
                cityCounter++;
            }
        }
        if (cityCounter == cityNumber){
            return true;
        }
        return false;
    }

    public String getRegion() {
        return region;
    }
    @Override
    public Council getCouncil(){
        return council;
    }

    @Override
    public String toString() {
        return "Region{" +
                "cityNumber=" + cityNumber +
                ", region='" + region + '\'' +
                ", council=" + council +
                '}';
    }

    public static void main(String[] args){
        System.out.println(Region.COAST);
        Gson gson = new GsonBuilder().registerTypeAdapter(Action.class, new InterfaceAdapter<Action>())
                .registerTypeAdapter(Region.class, new InterfaceAdapter<>())
                .create();
        String region = gson.toJson(Region.COAST);
        System.out.println(region);
    }


}
