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


    private final RegionName region;
    private final int cityNumber;
    private Council council;
    private Bank bank;

    //default
    public Region(RegionName regionName, int cityNumber, Bank bank) {
        region = regionName;
        this.cityNumber = cityNumber;
        this.bank = bank;
        this.createRandomCouncil();

    }

    public Region(RegionName region, int cityNumber) {
        this.region = region;
        this.cityNumber = cityNumber;
        this.createRandomCouncil();
    }

    private void createRandomCouncil() {
        if (this.council == null) {
            this.council = new Council(this.bank);
            Random random = new Random();
            for (int i = 0; i < Constants.COUNCILOR_DIMENSION; i++) {
                ArrayList<PoliticColor> availablePoliticColors = this.bank.showCouncilor();
                int value = random.nextInt(availablePoliticColors.size());
                Councilor toAdd = this.bank.getCouncilor(availablePoliticColors.get(value));
                if (toAdd != null) {
                    this.council.add(toAdd);
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
            if (city.getRegion() == region) {
                cityCounter++;
            }
        }
        return cityCounter == this.cityNumber;
    }

    public RegionName getRegion() {
        return this.region;
    }

    @Override
    public Council getCouncil() {
        return this.council;
    }

    @Override
    public String toString() {
        return "Region{" +
                "cityNumber=" + this.cityNumber +
                ", region='" + this.region + '\'' +
                ", council=" + this.council +
                '}';
    }


}
