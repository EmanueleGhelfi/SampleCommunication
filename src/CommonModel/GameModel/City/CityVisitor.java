package CommonModel.GameModel.City;

import org.jgrapht.UndirectedGraph;
import org.jgrapht.alg.NeighborIndex;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Emanuele on 15/05/2016.
 */
public class CityVisitor {

    private UndirectedGraph<City, DefaultEdge> cities = new SimpleGraph<City, DefaultEdge>(DefaultEdge.class);
    private final HashMap<City, Boolean> alreadyVisited = new HashMap<>();
    private final ArrayList<City> usersEmporium;
    private final NeighborIndex neighborIndex;

    public CityVisitor(UndirectedGraph<City, DefaultEdge> cities, ArrayList<City> usersEmporium) {
        //this.usersEmporium = user;
        this.cities = cities;
        this.usersEmporium = usersEmporium;
        this.neighborIndex = new NeighborIndex(cities);
    }

    public ArrayList<City> visit(City city) {
        ArrayList<City> visitedCity = new ArrayList<>();
        this.alreadyVisited.put(city, true);
        for (Object city1 : this.neighborIndex.neighborListOf(city)) {
            City realCity = (City) city1;
            if (!this.alreadyVisited.containsKey(realCity) && this.usersEmporium.contains(realCity) && !realCity.equals(city)) {
                this.alreadyVisited.put(realCity, true);
                visitedCity.add(realCity);
                visitedCity.addAll(this.visit(realCity));
            } else {
            }
        }
        return visitedCity;
    }

}
