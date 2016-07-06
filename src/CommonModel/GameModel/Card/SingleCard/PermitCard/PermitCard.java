package CommonModel.GameModel.Card.SingleCard.PermitCard;

import CommonModel.GameModel.Bonus.Generic.Bonus;
import CommonModel.GameModel.City.CityName;
import CommonModel.GameModel.City.RegionName;
import CommonModel.GameModel.Market.BuyableObject;
import Utilities.Class.Constants;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Giulio on 13/05/2016.
 */
public class PermitCard implements Serializable, BuyableObject {

    private Bonus bonus;
    private ArrayList<Character> cityAcronimous;
    private RegionName retroType;

    public PermitCard(Bonus bonus, ArrayList<CityName> cityAcronimous, RegionName retroType) {
        this.bonus = bonus;
        //this.cityAcronimous = cityAcronimous;
        this.retroType = retroType;
    }

    public PermitCard() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PermitCard that = (PermitCard) o;
        if (cityAcronimous != null ? !cityAcronimous.equals(that.cityAcronimous) : that.cityAcronimous != null)
            return false;
        return retroType == that.retroType;
    }

    @Override
    public int hashCode() {
        int result = cityAcronimous != null ? cityAcronimous.hashCode() : 0;
        result = 31 * result + retroType.hashCode();
        return result;
    }

    public Bonus getBonus() {
        return bonus;
    }

    public void setBonus(Bonus bonus) {
        this.bonus = bonus;
    }

    public ArrayList<Character> getCityAcronimous() {
        return cityAcronimous;
    }

    public void setCityAcronimous(ArrayList<Character> cityAcronimous) {
        this.cityAcronimous = cityAcronimous;
    }

    public RegionName getRetroType() {
        return retroType;
    }

    public void setRetroType(RegionName retroType) {
        this.retroType = retroType;
    }

    public String getCityString() {
        String cityString = "";
        for (char cityCharacter : cityAcronimous) {
            cityString += cityCharacter + " / ";
        }
        cityString = cityString.substring(0, cityString.length() - 2);
        return cityString;
    }


    public String getType() {
        return Constants.PERMIT_CARD;
    }

    @Override
    public String getInfo() {
        return getCityString();
    }

    @Override
    public BuyableObject getCopy() {
        try {
            return (BuyableObject) this.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getUrl() {
        return "PermitCard";
    }

    @Override
    public String toString() {
        String bonusInfo = "";
        /*
        for(Bonus singleBonus: bonus.getBonusArrayList()){
            bonusInfo+= singleBonus.getBonusName()+" "+singleBonus.getBonusInfo()+"\n";
        }
        */
        return "PermitCard \n" +
                "cityAcronimous=" + cityAcronimous +
                ",\nretroType=" + retroType +
                ",\nbonus=" + bonus.toString() + " \n";
    }
}
