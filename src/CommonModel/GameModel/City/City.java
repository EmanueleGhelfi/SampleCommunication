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
        bonus = null;
    }

    public void createRandomBonus() {
        bonus = new MainBonus(1, 2, 5, false);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        City city = (City) o;
        if (cityName != city.cityName) return false;
        if (cityColor != city.cityColor) return false;
        return cityRegion == city.cityRegion;
    }

    @Override
    public int hashCode() {
        int result = cityName != null ? cityName.hashCode() : 0;
        result = 31 * result + (cityColor != null ? cityColor.hashCode() : 0);
        result = 31 * result + (cityRegion != null ? cityRegion.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "City{" +
                "cityRegion=" + cityRegion +
                ", cityName=" + cityName +
                ", cityColor=" + cityColor +
                ", bonus=" + bonus +
                '}';
    }

    public Bonus getBonus() {
        return bonus;
    }

    public RegionName getRegion() {
        return cityRegion;
    }

    public Color getColor() {
        return cityColor;
    }

    public CityName getCityName() {
        return cityName;
    }

    public void getBonus(User user, Game game) throws ActionNotPossibleException {
        bonus.getBonus(user, game);
    }
}
