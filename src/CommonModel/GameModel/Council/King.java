package CommonModel.GameModel.Council;

import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticColor;
import CommonModel.GameModel.City.*;
import Utilities.Class.Constants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Giulio on 14/05/2016.
 */
public class King implements Serializable, GotCouncil {

    private City currentCity;
    private Council council;
    private Bank bank;

    public King() {
        this.council = new Council(bank);
        Random random = new Random();
        for(int i = 0; i< Constants.COUNCILOR_DIMENSION; i++){
            PoliticColor[] politicColors = PoliticColor.values();
            int value = random.nextInt(5);

            council.add(new Councilor(politicColors[value]));
        }
        this.currentCity = new City(Color.BLUE, CityName.ARKON, RegionName.COAST);
    }

    public King(City currentCity, Bank bank) {
        this.bank = bank;
        this.council = new Council(bank);
        Random random = new Random();
        for(int i = 0; i< Constants.COUNCILOR_DIMENSION; i++){
            ArrayList<PoliticColor> politicColors = bank.showCouncilor();
            int value = random.nextInt(politicColors.size());
            Councilor toAdd = bank.getCouncilor(politicColors.get(value));
            if(toAdd!=null) {
                council.add(toAdd);
            }
            else {
                // retry random
                i--;
            }
        }
        this.currentCity = currentCity;
    }

    @Override
    public Council getCouncil() {
        return council;
    }

    public City getCurrentCity() {
        return currentCity;
    }

    public void setCurrentCity(City currentCity) {
        this.currentCity = currentCity;
    }
}