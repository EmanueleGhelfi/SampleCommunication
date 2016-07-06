package CommonModel.GameModel.City;

import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticColor;
import CommonModel.GameModel.Council.Bank;
import CommonModel.GameModel.Council.Council;
import CommonModel.GameModel.Council.Councilor;
import CommonModel.GameModel.Council.GotCouncil;
import Utilities.Class.Constants;

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
    private Bank bank;

    //default
    public Region(RegionName regionName, int cityNumber, Bank bank) {
        this.region = regionName;
        this.cityNumber = cityNumber;
        this.bank = bank;
        createRandomCouncil();

    }

    public Region(RegionName region, int cityNumber) {
        this.region = region;
        this.cityNumber = cityNumber;
        createRandomCouncil();
    }

    private void createRandomCouncil() {
        if (council == null) {
            council = new Council(bank);
            Random random = new Random();
            for (int i = 0; i < Constants.COUNCILOR_DIMENSION; i++) {
                ArrayList<PoliticColor> availablePoliticColors = bank.showCouncilor();
                int value = random.nextInt(availablePoliticColors.size());
                Councilor toAdd = bank.getCouncilor(availablePoliticColors.get(value));
                if (toAdd != null) {
                    council.add(toAdd);
                } else {
                    // retry random
                    i--;
                }
            }
        }
    }

    public boolean checkRegion(ArrayList<City> userEmporiums) {
        int cityCounter = 0;
        for (City city : userEmporiums) {
            if (city.getRegion() == this.region) {
                cityCounter++;
            }
        }
        return cityCounter == cityNumber;
    }

    public RegionName getRegion() {
        return region;
    }

    @Override
    public Council getCouncil() {
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


}
