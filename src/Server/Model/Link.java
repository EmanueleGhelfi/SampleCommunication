package Server.Model;

import CommonModel.GameModel.City.City;

/**
 * Created by Emanuele on 20/05/2016.
 */
public class Link {

    private City city1;
    private City city2;

    public Link() {
    }

    public Link(City city1, City city2) {
        this.city1 = city1;
        this.city2 = city2;
    }
}
