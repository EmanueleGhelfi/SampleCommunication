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


    public static void main(String[] args){
        City city = new City(Color.BLUE,CityName.ARKON,Region.COAST);
        City city2 = new City(Color.GREY,CityName.BURGEN,Region.COAST);
        City city3 = new City(Color.BLUE,CityName.KULTOS,Region.COAST);
        City city4 = new City(Color.GREY,CityName.NARIS,Region.COAST);
        City city5 = new City(Color.BLUE,CityName.OSIUM,Region.COAST);
        City city6 = new City(Color.GREY,CityName.GRADEN,Region.COAST);
        UndirectedGraph<City,DefaultEdge> graph = new SimpleGraph<City, DefaultEdge>(DefaultEdge.class);
        graph.addVertex(city);
        graph.addVertex(city2);
        graph.addVertex(city3);
        graph.addVertex(city4);
        graph.addVertex(city5);
        graph.addVertex(city6);
        graph.addEdge(city,city2);
        graph.addEdge(city2,city5);
        graph.addEdge(city2,city4);
        graph.addEdge(city3,city4);
        graph.addEdge(city4,city5);
        ArrayList<City> usersEmporium = new ArrayList<>();
        usersEmporium.add(city2);
        usersEmporium.add(city3);
        usersEmporium.add(city4);
        System.out.println("MAIN: " +usersEmporium.contains(city2));
        System.out.println("EMPORIUMS");
        CityVisitor cityVisitor = new CityVisitor(graph, usersEmporium);
        ArrayList<City> cities = cityVisitor.visit(city2);
        System.out.println(cities);
    }
}
