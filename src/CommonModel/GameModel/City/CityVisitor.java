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
public class CityVisitor{

    private UndirectedGraph<City,DefaultEdge> cities = new SimpleGraph<City, DefaultEdge>(DefaultEdge.class);
    private HashMap<City,Boolean> alreadyVisited = new HashMap<>();
    private ArrayList<City> usersEmporium;
    private NeighborIndex neighborIndex;

    public CityVisitor(UndirectedGraph<City, DefaultEdge> cities,ArrayList<City> usersEmporium) {
        //this.usersEmporium = user;
        this.cities = cities;
        this.usersEmporium = usersEmporium;
        neighborIndex = new NeighborIndex(cities);
    }

    public ArrayList<City> visit(City city){
        System.out.println("Visiting "+city.toString());
        ArrayList<City> visitedCity = new ArrayList<>();
        alreadyVisited.put(city,true);
        for (Object city1: neighborIndex.neighborListOf(city)) {
            City realCity = (City)city1;
            System.out.println("FOR" + city1);
            if(!alreadyVisited.containsKey(realCity) && usersEmporium.contains(realCity) && !realCity.equals(city)){
                alreadyVisited.put(realCity,true);
                visitedCity.add(realCity);
                visitedCity.addAll(visit(realCity));
            }
            else{
                System.out.println("In else");
            }
        }
        return visitedCity;
    }
}
