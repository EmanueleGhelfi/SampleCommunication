package Server.Model;

import CommonModel.GameModel.Action.Action;
import CommonModel.GameModel.Bonus.Generic.Bonus;
import CommonModel.GameModel.City.City;
import CommonModel.GameModel.City.CityName;
import CommonModel.GameModel.City.Color;
import CommonModel.GameModel.City.Region;
import Utilities.Class.InterfaceAdapter;
import Utilities.Exception.MapsNotFoundException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.io.*;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Created by Emanuele on 20/05/2016.
 */
public class Map implements Serializable {


    private ArrayList<Link> links;


    private ArrayList<City> city;


    private String mapName;


    private String mapPreview;


    private transient SimpleGraph<City,DefaultEdge> mapGraph= new SimpleGraph<>(DefaultEdge.class);




    public Map() {
    }

    public ArrayList<City> getCity() {
        return city;
    }

    public void setCity(ArrayList<City> city) {
        this.city = city;
    }


    public ArrayList<Link> getLinks() {
        return links;
    }

    public void setLinks(ArrayList<Link> links) {
        this.links = links;
    }

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public String getMapPreview() {
        return mapPreview;
    }

    public void setMapPreview(String mapPreview) {
        this.mapPreview = mapPreview;
    }

    public UndirectedGraph<City, DefaultEdge> getMapGraph() {
        return mapGraph;
    }

    public void setMapGraph(SimpleGraph<City, DefaultEdge> mapGraph) {
        this.mapGraph = mapGraph;
    }

    @Override
    public String toString() {
        return "Map{" +
                "city=" + city +
                ", links=" + links +
                ", mapName='" + mapName + '\'' +
                ", mapPreview='" + mapPreview + '\'' +
                '}';
    }

    //added equals
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Map map = (Map) o;

        if (links != null ? !links.equals(map.links) : map.links != null) return false;
        if (city != null ? !city.equals(map.city) : map.city != null) return false;
        if (mapName != null ? !mapName.equals(map.mapName) : map.mapName != null) return false;
        return mapPreview != null ? mapPreview.equals(map.mapPreview) : map.mapPreview == null;

    }

    @Override
    public int hashCode() {
        int result = links != null ? links.hashCode() : 0;
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (mapName != null ? mapName.hashCode() : 0);
        result = 31 * result + (mapPreview != null ? mapPreview.hashCode() : 0);
        return result;
    }

    public static void write(){
        City arkon = new City(Color.BLUE, CityName.ARKON, Region.COAST);
        City burgen = new City(Color.YELLOW,CityName.BURGEN,Region.COAST);
        City castrum = new City(Color.GREY,CityName.CASTRUM,Region.COAST);
        City dorful = new City(Color.BLUE,CityName.DORFUL,Region.COAST);
        City esti = new City(Color.GREY,CityName.ESTI,Region.COAST);
        City framek = new City(Color.YELLOW,CityName.FRAMEK,Region.HILL);
        City indur = new City(Color.ORANGE,CityName.INDUR,Region.HILL);
        City graden = new City(Color.GREY,CityName.GRADEN,Region.HILL);
        City juvelar = new City(Color.PURPLE,CityName.JUVELAR,Region.HILL);
        City hellar = new City(Color.YELLOW,CityName.HELLAR,Region.HILL);
        City kultos = new City(Color.YELLOW,CityName.KULTOS,Region.MOUNTAIN);
        City lyram = new City(Color.BLUE,CityName.LYRAM,Region.MOUNTAIN);
        City merkatim = new City(Color.GREY,CityName.MERKATIM,Region.MOUNTAIN);
        City naris = new City(Color.ORANGE,CityName.NARIS,Region.MOUNTAIN);
        City osium = new City(Color.YELLOW,CityName.OSIUM,Region.MOUNTAIN);

        ArrayList<Link> links = new ArrayList<>();
        links.add(new Link(arkon,castrum));
        links.add(new Link(arkon,burgen));
        links.add(new Link(burgen,dorful));
        links.add(new Link(burgen,esti));
        links.add(new Link(castrum,framek));
        links.add(new Link(dorful,graden));
        links.add(new Link(hellar,esti));
        links.add(new Link(framek,indur));
        links.add(new Link(graden,juvelar));
        links.add(new Link(hellar,juvelar));
        links.add(new Link(indur,juvelar));
        links.add(new Link(indur,kultos));
        links.add(new Link(juvelar,lyram));
        links.add(new Link(hellar,merkatim));
        links.add(new Link(merkatim,osium));
        links.add(new Link(osium,lyram));
        links.add(new Link(osium,naris));
        links.add(new Link(naris,kultos));

        ArrayList<City> cities = new ArrayList<>();
        cities.add(arkon);
        cities.add(burgen);
        cities.add(castrum);
        cities.add(dorful);
        cities.add(esti);
        cities.add(framek);
        cities.add(indur);
        cities.add(graden);
        cities.add(juvelar);
        cities.add(hellar);
        cities.add(kultos);
        cities.add(lyram);
        cities.add(merkatim);
        cities.add(naris);
        cities.add(osium);

        Map map = new Map();
        map.setCity(cities);
        map.setLinks(links);
        map.setMapName("prima mappa");
        map.setMapPreview("/ClientPackage/View/GUIResources/Image/Mappa1.jpg");

        System.out.println(map);
        Gson gson = new Gson();
        String gsonString = gson.toJson(map);
        System.out.println(gsonString);
        try {
            PrintWriter out = new PrintWriter("mapConfig.json");
            out.write(gsonString);
            out.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        try {
            ArrayList<Map> maps = readAllMap();
            Gson gson = new GsonBuilder().registerTypeAdapter(Action.class, new InterfaceAdapter<Action>())
                    .registerTypeAdapter(Bonus.class,new InterfaceAdapter<Bonus>())
                    .excludeFieldsWithModifiers(Modifier.TRANSIENT)
                    .create();
            String json = gson.toJson(maps,new TypeToken<ArrayList<Map>>(){}.getType());
            System.out.println(json);
            ArrayList<Map> maps2 = gson.fromJson(json,new TypeToken<ArrayList<Map>>(){}.getType());
        } catch (MapsNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void read(){
        try {
            //BufferedReader fileReader = new BufferedReader(new FileReader("/Utilities/ConfigurationFile/mapConfig.json"));
            String s="";
            String text = new String(Files.readAllBytes(Paths.get("src/Utilities/ConfigurationFile/mapConfig.json")), StandardCharsets.UTF_8);
            System.out.println(text);
            Gson gson = new GsonBuilder().excludeFieldsWithModifiers(Modifier.VOLATILE).create();
            Map map =gson.fromJson(text,Map.class);
            System.out.println(map);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Map> readAllMap() throws MapsNotFoundException {
        File file = new File("src/Utilities/ConfigurationFile/");
        ArrayList<Map> maps = new ArrayList<>();
        Gson gson = new Gson();
        if(file.exists()) {
            for (File fileMap : file.listFiles()) {
                try {
                    String text = new String(Files.readAllBytes(fileMap.toPath()), StandardCharsets.UTF_8);
                    Map map = gson.fromJson(text,Map.class);
                    UndirectedGraph graph = map.getMapGraph();
                    for (City city: map.getCity()) {
                        city.createRandomBonus();
                        graph.addVertex(city);
                    }
                    for(Link links: map.getLinks())
                    {
                        graph.addEdge(links.getCity1(),links.getCity2());
                    }
                    maps.add(map);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            return maps;
        }
        throw new MapsNotFoundException();
    }

}
