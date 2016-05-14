package CommonModel.GameModel;

import java.util.ArrayList;

/**
 * Created by Giulio on 13/05/2016.
 */
public class PermitCard {

    private Bonus bonus;
    private ArrayList<CityName> cityAcronimous;
    private Region retroType;

    public PermitCard(Bonus bonus, ArrayList<CityName> cityAcronimous, Region retroType) {
        this.bonus = bonus;
        this.cityAcronimous = cityAcronimous;
        this.retroType = retroType;
    }

    public PermitCard() {
    }

    public Bonus getBonus() {
        return bonus;
    }

    public void setBonus(Bonus bonus) {
        this.bonus = bonus;
    }

    public ArrayList<CityName> getCityAcronimous() {
        return cityAcronimous;
    }

    public void setCityAcronimous(ArrayList<CityName> cityAcronimous) {
        this.cityAcronimous = cityAcronimous;
    }

    public Region getRetroType() {
        return retroType;
    }

    public void setRetroType(Region retroType) {
        this.retroType = retroType;
    }
}
