package CommonModel.GameModel.City;

import CommonModel.GameModel.Bonus.Generic.Bonus;
import CommonModel.GameModel.Bonus.Generic.MainBonus;

/**
 * Created by Giulio on 13/05/2016.
 */
public class City {

    private CityName cityName;
    private Color cityColor;
    private Region cityRegion;
    private Bonus bonus;

    public City(Color cityColor, CityName cityName, Region cityRegion) {
        this.cityColor = cityColor;
        this.cityName = cityName;
        this.cityRegion = cityRegion;
        bonus = new MainBonus(1,2,5,false);
    }

    public CityName getCityName() {
        return cityName;
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

    public Bonus getBonus() {
        return bonus;
    }

    public Region getRegion() {
        return cityRegion;
    }

    public Color getColor() {
        return cityColor;
    }
}
