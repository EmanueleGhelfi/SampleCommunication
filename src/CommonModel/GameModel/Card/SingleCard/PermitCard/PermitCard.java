package CommonModel.GameModel.Card.SingleCard.PermitCard;

import CommonModel.GameModel.Bonus.Generic.Bonus;
import CommonModel.GameModel.City.CityName;
import CommonModel.GameModel.City.Region;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Giulio on 13/05/2016.
 */
public class PermitCard implements Serializable{

    private Bonus bonus;
    private ArrayList<Character> cityAcronimous;
    private Region retroType;

    public PermitCard(Bonus bonus, ArrayList<CityName> cityAcronimous, Region retroType) {
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
    public void setCityAcronimous(ArrayList<Character> cityAcronimous) {
        this.cityAcronimous = cityAcronimous;
    }

    public ArrayList<Character> getCityAcronimous() {
        return cityAcronimous;
    }

    public void setRetroType(Region retroType) {
        this.retroType = retroType;
    }
}
