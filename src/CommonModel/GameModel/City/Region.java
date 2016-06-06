package CommonModel.GameModel.City;

import CommonModel.GameModel.Action.Action;
import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticCard;
import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticColor;
import CommonModel.GameModel.Council.Council;
import CommonModel.GameModel.Council.Councilor;
import CommonModel.GameModel.Council.GotCouncil;
import Utilities.Class.Constants;
import Utilities.Class.EnumAdapterFactory;
import Utilities.Class.InterfaceAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Giulio on 13/05/2016.
 */
public class Region implements Serializable, GotCouncil {



    private RegionName region;
    private int cityNumber;
    private Council council;

    //default
    public Region() {

    }

    public Region (RegionName region, int cityNumber){
        this.region = region;
        this.cityNumber = cityNumber;
        createRandomCouncil();
    }

    private void createRandomCouncil(){
        if(council==null) {
            council = new Council();
            Random random = new Random();
            for (int i = 0; i < Constants.COUNCILOR_DIMENSION; i++) {
                PoliticColor[] politicColors = PoliticColor.values();
                int value = random.nextInt(5);
                council.add(new Councilor(politicColors[value]));
            }
        }
    }

    public boolean checkRegion(ArrayList<City> userEmporiums){
        int cityCounter = 0;
        for (City city: userEmporiums) {
            if (city.getRegion() == this.region) {
                cityCounter++;
            }
        }
        if (cityCounter == cityNumber){
            return true;
        }
        return false;
    }

    public RegionName getRegion() {
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
        //System.out.println(Region.COAST);
        Gson gson = new GsonBuilder().registerTypeAdapter(Action.class, new InterfaceAdapter<Action>())
                .registerTypeAdapterFactory(new EnumAdapterFactory())
                .create();
        //String region = gson.toJson(Region.COAST);
        //System.out.println(region);
    }


}
