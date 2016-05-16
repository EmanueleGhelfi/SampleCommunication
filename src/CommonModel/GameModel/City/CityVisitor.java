package CommonModel.GameModel.City;

import Server.UserClasses.User;

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

    private User user;

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
        City framek = new City(Color.BLUE,CityName.FRAMEK,Region.COAST);
        City indur = new City(Color.GREY,CityName.INDUR,Region.COAST);
        City juvelar = new City(Color.BLUE,CityName.JUVELAR,Region.COAST);
        City graden = new City(Color.GREY,CityName.GRADEN,Region.COAST);
        City hellar = new City(Color.BLUE,CityName.HELLAR,Region.COAST);
        City castrum = new City(Color.GREY,CityName.CASTRUM,Region.COAST);
        City arkon = new City(Color.GREY,CityName.ARKON,Region.COAST);
        City burgen = new City(Color.BLUE,CityName.BURGEN,Region.COAST);
        City dorful = new City(Color.GREY,CityName.DORFUL,Region.COAST);
        City esti = new City(Color.GREY,CityName.GRADEN,Region.COAST);

        UndirectedGraph<City,DefaultEdge> graph = new SimpleGraph<City, DefaultEdge>(DefaultEdge.class);
        graph.addVertex(framek);
        graph.addVertex(indur);
        graph.addVertex(juvelar);
        graph.addVertex(graden);
        graph.addVertex(hellar);
        graph.addVertex(castrum);
        graph.addVertex(arkon);
        graph.addVertex(burgen);
        graph.addVertex(dorful);
        graph.addVertex(esti);

        graph.addEdge(framek,indur);
        graph.addEdge(graden,juvelar);
        graph.addEdge(juvelar,indur);
        graph.addEdge(juvelar,hellar);
        graph.addEdge(castrum,framek);
        graph.addEdge(arkon,castrum);
        graph.addEdge(arkon,burgen);
        graph.addEdge(burgen,dorful);
        graph.addEdge(burgen,esti);
        graph.addEdge(dorful,graden);
        graph.addEdge(esti,hellar);
        ArrayList<City> usersEmporium = new ArrayList<>();
        usersEmporium.add(framek);
        usersEmporium.add(indur);
        usersEmporium.add(juvelar);
        usersEmporium.add(graden);
        usersEmporium.add(esti);
        System.out.println("MAIN: " +usersEmporium.contains(framek));
        System.out.println("EMPORIUMS");
        CityVisitor cityVisitor = new CityVisitor(graph, usersEmporium);
        ArrayList<City> cities = cityVisitor.visit(framek);
        System.out.println(cities);


    }

}
