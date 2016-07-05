package CommonModel.GameModel.City;

import CommonModel.GameModel.Bonus.Generic.Bonus;
import CommonModel.GameModel.Bonus.Generic.MainBonus;
import Server.Model.Game;
import Server.Model.User;
import Utilities.Exception.ActionNotPossibleException;

import java.io.Serializable;

/**
 * Created by Giulio on 13/05/2016.
 */
public class City implements Serializable {


    private CityName cityName;

    private Color cityColor;

    private RegionName cityRegion;

    private String urlCity;

    private Bonus bonus;

    public City() {
    }

    public City(Color cityColor, CityName cityName, RegionName cityRegion) {
        this.cityColor = cityColor;
        this.cityName = cityName;
        this.cityRegion = cityRegion;
        this.bonus = null;
    }

    public void createRandomBonus() {
        this.bonus = new MainBonus(1, 2, 5, false);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        City city = (City) o;
        if (this.cityName != city.cityName) return false;
        if (this.cityColor != city.cityColor) return false;
        return this.cityRegion == city.cityRegion;
    }

    @Override
    public int hashCode() {
        int result = this.cityName != null ? this.cityName.hashCode() : 0;
        result = 31 * result + (this.cityColor != null ? this.cityColor.hashCode() : 0);
        result = 31 * result + (this.cityRegion != null ? this.cityRegion.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "City{" +
                "cityRegion=" + this.cityRegion +
                ", cityName=" + this.cityName +
                ", cityColor=" + this.cityColor +
                ", bonus=" + this.bonus +
                '}';
    }

    public Bonus getBonus() {
        return this.bonus;
    }

    public RegionName getRegion() {
        return this.cityRegion;
    }

    public Color getColor() {
        return this.cityColor;
    }

    public CityName getCityName() {
        return this.cityName;
    }

    public void getBonus(User user, Game game) throws ActionNotPossibleException {
        this.bonus.getBonus(user, game);
    }
}
