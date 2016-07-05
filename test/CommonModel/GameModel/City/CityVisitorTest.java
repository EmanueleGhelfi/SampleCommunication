package CommonModel.GameModel.City;

import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by Emanuele on 29/06/2016.
 */
public class CityVisitorTest {
    @Before
    public void setUp() throws Exception {


    }


    // testing linked city
    @Test
    public void test() {
        City city = new City(Color.BLUE, CityName.ARKON, RegionName.COAST);
        City city2 = new City(Color.GREY, CityName.BURGEN, RegionName.COAST);
        City city3 = new City(Color.BLUE, CityName.KULTOS, RegionName.COAST);
        City city4 = new City(Color.GREY, CityName.NARIS, RegionName.COAST);
        City city5 = new City(Color.BLUE, CityName.OSIUM, RegionName.COAST);
        City city6 = new City(Color.GREY, CityName.GRADEN, RegionName.COAST);
        UndirectedGraph<City, DefaultEdge> graph = new SimpleGraph<City, DefaultEdge>(DefaultEdge.class);
        graph.addVertex(city);
        graph.addVertex(city2);
        graph.addVertex(city3);
        graph.addVertex(city4);
        graph.addVertex(city5);
        graph.addVertex(city6);
        graph.addEdge(city, city2);
        graph.addEdge(city2, city5);
        graph.addEdge(city2, city4);
        graph.addEdge(city3, city4);
        graph.addEdge(city4, city5);
        ArrayList<City> usersEmporium = new ArrayList<>();
        usersEmporium.add(city2);
        usersEmporium.add(city3);
        usersEmporium.add(city4);
        CityVisitor cityVisitor = new CityVisitor(graph, usersEmporium);
        ArrayList<City> cities = cityVisitor.visit(city2);
        System.out.println(cities);
        ArrayList<City> visited = new ArrayList<>();
        visited.add(city4);
        visited.add(city3);
        assertEquals(cities, visited);
    }
}