package CommonModel.GameModel.Council;

import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticColor;
import CommonModel.GameModel.City.City;
import CommonModel.GameModel.City.CityName;
import CommonModel.GameModel.City.Color;
import CommonModel.GameModel.City.RegionName;
import Utilities.Class.Constants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Giulio on 14/05/2016.
 */
public class King implements Serializable, GotCouncil {

    private City currentCity;
    private final Council council;
    private Bank bank;

    public King() {
        council = new Council(this.bank);
        Random random = new Random();
        for (int i = 0; i < Constants.COUNCILOR_DIMENSION; i++) {
            PoliticColor[] politicColors = PoliticColor.values();
            int value = random.nextInt(5);

            this.council.add(new Councilor(politicColors[value]));
        }
        currentCity = new City(Color.BLUE, CityName.ARKON, RegionName.COAST);
    }

    public King(City currentCity, Bank bank) {
        this.bank = bank;
        council = new Council(bank);
        Random random = new Random();
        for (int i = 0; i < Constants.COUNCILOR_DIMENSION; i++) {
            ArrayList<PoliticColor> politicColors = bank.showCouncilor();
            int value = random.nextInt(politicColors.size());
            Councilor toAdd = bank.getCouncilor(politicColors.get(value));
            if (toAdd != null) {
                this.council.add(toAdd);
            } else {
                // retry random
                i--;
            }
        }
        this.currentCity = currentCity;
    }

    @Override
    public Council getCouncil() {
        return this.council;
    }

    public City getCurrentCity() {
        return this.currentCity;
    }

    public void setCurrentCity(City currentCity) {
        this.currentCity = currentCity;
    }
}