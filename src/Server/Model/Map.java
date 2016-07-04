package Server.Model;

import CommonModel.GameModel.Action.Action;
import CommonModel.GameModel.Bonus.Generic.Bonus;
import CommonModel.GameModel.City.*;
import Utilities.Class.Constants;
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

    private String realMap;


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

    private void setRealMap(String realMap) {
        this.realMap = realMap;
    }

    public String getRealMap() {
        return realMap;
    }

    public static void writeMap1(){


        Map map = writeGenericMap(1,1,1,"Prima Mappa","/ClientPackage/View/GUIResources/Image/Map/AAA.png","/ClientPackage/View/GUIResources/Image/MapPreview/AAA.png");
        Map map2 = writeGenericMap(2,2,2,"Seconda Mappa","/ClientPackage/View/GUIResources/Image/Map/BBB.png","/ClientPackage/View/GUIResources/Image/MapPreview/BBB.png");
        Map map3 = writeGenericMap(1,2,2,"Terza Mappa", "/ClientPackage/View/GUIResources/Image/Map/ABB.png", "/ClientPackage/View/GUIResources/Image/MapPreview/ABB.png");
        Map map4 = writeGenericMap(1,1,2,"Quarta Mappa","/ClientPackage/View/GUIResources/Image/Map/AAB.png","/ClientPackage/View/GUIResources/Image/MapPreview/AAB.png");
        Map map5 = writeGenericMap(1,2,1,"Quinta Mappa", "/ClientPackage/View/GUIResources/Image/Map/ABA.png","/ClientPackage/View/GUIResources/Image/MapPreview/ABA.png");
        Map map6 = writeGenericMap(2,1,1,"Sesta Mappa", "/ClientPackage/View/GUIResources/Image/Map/BAA.png","/ClientPackage/View/GUIResources/Image/MapPreview/BAA.png");
        Map map7 = writeGenericMap(2,1,2,"Settima mappa", "/ClientPackage/View/GUIResources/Image/Map/BAB.png","/ClientPackage/View/GUIResources/Image/MapPreview/BAB.png");
        Map map8 = writeGenericMap(2,2,1,"Ottava Mappa","/ClientPackage/View/GUIResources/Image/Map/BBA.png","/ClientPackage/View/GUIResources/Image/MapPreview/BBA.png");

        ArrayList<Map> mapArray = new ArrayList<>();
        mapArray.add(map);
        mapArray.add(map2);
        mapArray.add(map3);
        mapArray.add(map4);
        mapArray.add(map5);
        mapArray.add(map6);
        mapArray.add(map7);
        mapArray.add(map8);

        for(Map singleMap: mapArray) {
            System.out.println(singleMap);
            Gson gson = new Gson();
            String gsonString = gson.toJson(singleMap);
            System.out.println(gsonString);
            try {
                PrintWriter out = new PrintWriter("src/Utilities/ConfigurationFile/mapConfig"+mapArray.indexOf(singleMap)+".json");
                out.write(gsonString);
                out.flush();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static Map writeGenericMap(int firstPart, int secondPart, int thirdPart, String mapName, String mapUrl, String mapPreview) {
        City arkon;
        City burgen;
        City castrum;
        City dorful;
        City esti;
        City framek;
        City graden;
        City hellar;
        City indur;
        City juvelar;
        City kultos;
        City lyram;
        City merkatim;
        City naris;
        City osium;
        ArrayList<Link> links = new ArrayList<>();

        switch (firstPart){
            case 1:
                 arkon = new City(Color.BLUE, CityName.ARKON, RegionName.COAST);
                 burgen = new City(Color.YELLOW,CityName.BURGEN,RegionName.COAST);
                 castrum = new City(Color.GREY,CityName.CASTRUM,RegionName.COAST);
                 dorful = new City(Color.GREY,CityName.DORFUL,RegionName.COAST);
                 esti = new City(Color.ORANGE,CityName.ESTI,RegionName.COAST);
                links.add(new Link(arkon,castrum));
                links.add(new Link(arkon,burgen));
                links.add(new Link(burgen,dorful));
                links.add(new Link(burgen,esti));
                break;
            case 2:
                 arkon = new City(Color.YELLOW, CityName.ARKON, RegionName.COAST);
                 burgen = new City(Color.ORANGE,CityName.BURGEN,RegionName.COAST);
                 castrum = new City(Color.GREY,CityName.CASTRUM,RegionName.COAST);
                 dorful = new City(Color.BLUE,CityName.DORFUL,RegionName.COAST);
                 esti = new City(Color.GREY,CityName.ESTI,RegionName.COAST);
                links.add(new Link(arkon,castrum));
                links.add(new Link(esti,burgen));
                links.add(new Link(castrum,dorful));
                break;
            default:
                arkon = new City(Color.BLUE, CityName.ARKON, RegionName.COAST);
                burgen = new City(Color.YELLOW,CityName.BURGEN,RegionName.COAST);
                castrum = new City(Color.GREY,CityName.CASTRUM,RegionName.COAST);
                dorful = new City(Color.GREY,CityName.DORFUL,RegionName.COAST);
                esti = new City(Color.ORANGE,CityName.ESTI,RegionName.COAST);
                links.add(new Link(arkon,castrum));
                links.add(new Link(arkon,burgen));
                links.add(new Link(burgen,dorful));
                links.add(new Link(burgen,esti));
                break;
        }

        switch (secondPart){
            case 1:
                 framek = new City(Color.YELLOW,CityName.FRAMEK,RegionName.HILL);
                 graden = new City(Color.PURPLE,CityName.GRADEN,RegionName.HILL);
                 hellar = new City(Color.YELLOW,CityName.HELLAR,RegionName.HILL);
                 indur = new City(Color.ORANGE,CityName.INDUR,RegionName.HILL);
                 juvelar = new City(Color.GREY,CityName.JUVELAR,RegionName.HILL);
                links.add(new Link(framek,indur));
                links.add(new Link(framek,graden));
                links.add(new Link(graden,hellar));
                links.add(new Link(graden,indur));
                links.add(new Link(hellar,juvelar));
                break;
            case 2:
                 framek = new City(Color.YELLOW,CityName.FRAMEK,RegionName.HILL);
                 graden = new City(Color.GREY,CityName.GRADEN,RegionName.HILL);
                 hellar = new City(Color.YELLOW,CityName.HELLAR,RegionName.HILL);
                 indur = new City(Color.ORANGE,CityName.INDUR,RegionName.HILL);
                 juvelar = new City(Color.PURPLE,CityName.JUVELAR,RegionName.HILL);
                links.add(new Link(framek,indur));
                links.add(new Link(graden,juvelar));
                links.add(new Link(hellar,juvelar));
                links.add(new Link(juvelar,indur));
                break;
            default:
                framek = new City(Color.YELLOW,CityName.FRAMEK,RegionName.HILL);
                graden = new City(Color.PURPLE,CityName.GRADEN,RegionName.HILL);
                hellar = new City(Color.YELLOW,CityName.HELLAR,RegionName.HILL);
                indur = new City(Color.ORANGE,CityName.INDUR,RegionName.HILL);
                juvelar = new City(Color.GREY,CityName.JUVELAR,RegionName.HILL);
                links.add(new Link(framek,indur));
                links.add(new Link(framek,graden));
                links.add(new Link(graden,hellar));
                links.add(new Link(graden,indur));
                links.add(new Link(hellar,juvelar));
                break;

        }

        switch (thirdPart){
            case 1:
                 kultos = new City(Color.YELLOW,CityName.KULTOS,RegionName.MOUNTAIN);
                 lyram = new City(Color.GREY,CityName.LYRAM,RegionName.MOUNTAIN);
                 merkatim = new City(Color.BLUE,CityName.MERKATIM,RegionName.MOUNTAIN);
                 naris = new City(Color.ORANGE,CityName.NARIS,RegionName.MOUNTAIN);
                 osium = new City(Color.YELLOW,CityName.OSIUM,RegionName.MOUNTAIN);
                links.add(new Link(merkatim,osium));
                links.add(new Link(osium,lyram));
                links.add(new Link(osium,naris));
                links.add(new Link(naris,kultos));
                break;
            case 2:
                 kultos = new City(Color.YELLOW,CityName.KULTOS,RegionName.MOUNTAIN);
                 lyram = new City(Color.BLUE,CityName.LYRAM,RegionName.MOUNTAIN);
                 merkatim = new City(Color.GREY,CityName.MERKATIM,RegionName.MOUNTAIN);
                 naris = new City(Color.ORANGE,CityName.NARIS,RegionName.MOUNTAIN);
                 osium = new City(Color.YELLOW,CityName.OSIUM,RegionName.MOUNTAIN);
                links.add(new Link(merkatim,osium));
                links.add(new Link(lyram,naris));
                links.add(new Link(lyram,kultos));
                break;
            default:
                kultos = new City(Color.YELLOW,CityName.KULTOS,RegionName.MOUNTAIN);
                lyram = new City(Color.GREY,CityName.LYRAM,RegionName.MOUNTAIN);
                merkatim = new City(Color.BLUE,CityName.MERKATIM,RegionName.MOUNTAIN);
                naris = new City(Color.ORANGE,CityName.NARIS,RegionName.MOUNTAIN);
                osium = new City(Color.YELLOW,CityName.OSIUM,RegionName.MOUNTAIN);
                links.add(new Link(merkatim,osium));
                links.add(new Link(osium,lyram));
                links.add(new Link(osium,naris));
                links.add(new Link(naris,kultos));
                break;
        }

        links.add(new Link(castrum,framek));
        links.add(new Link(dorful,graden));
        links.add(new Link(esti,hellar));
        links.add(new Link(indur,kultos));
        links.add(new Link(juvelar,lyram));
        if(secondPart==2){
            links.add(new Link(merkatim,juvelar));
        }
        else {
            links.add(new Link(merkatim, hellar));
        }

        ArrayList<City> cities = new ArrayList<>();
        cities.add(arkon);
        cities.add(burgen);
        cities.add(castrum);
        cities.add(dorful);
        cities.add(esti);
        cities.add(framek);
        cities.add(graden);
        cities.add(hellar);
        cities.add(indur);
        cities.add(juvelar);
        cities.add(kultos);
        cities.add(lyram);
        cities.add(merkatim);
        cities.add(naris);
        cities.add(osium);

        Map map = new Map();
        map.setCity(cities);
        map.setLinks(links);
        map.setMapName(mapName);
        map.setRealMap(mapUrl);
        map.setMapPreview(mapPreview);
        return map;
    }



    public static void main(String[] args){
        writeMap1();
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
                        if (!city.getColor().getColor().equals(Constants.PURPLE)) {
                            city.createRandomBonus();
                        }
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
