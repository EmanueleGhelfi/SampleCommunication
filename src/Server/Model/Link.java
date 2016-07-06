package Server.Model;

import ClientPackage.View.CLIResources.CLIColor;
import CommonModel.GameModel.City.City;

import java.io.Serializable;

/**
 * Created by Emanuele on 20/05/2016.
 */
public class Link implements Serializable {

    private City city1;
    private City city2;

    public Link() {
    }

    public Link(City city1, City city2) {
        this.city1 = city1;
        this.city2 = city2;
    }

    public City getCity1() {
        return city1;
    }

    public void setCity1(City city1) {
        this.city1 = city1;
    }

    public City getCity2() {
        return city2;
    }

    public void setCity2(City city2) {
        this.city2 = city2;
    }

    @Override
    public String toString() {
        return CLIColor.ANSI_BLUE + "Linked City \t " + CLIColor.ANSI_BLUE + city1.getCityName().getCityName() +
                " " + city2.getCityName().getCityName() + "";
    }
}
