package Server.Model;

import CommonModel.GameModel.City.City;
import CommonModel.GameModel.City.CityName;
import CommonModel.GameModel.City.Color;
import CommonModel.GameModel.City.Region;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Created by Emanuele on 20/05/2016.
 */
public class Map {

    private ArrayList<Link> links;

    private ArrayList<City> city;

    private String mapName;

    private String mapPreview;

    private String imageMapLeft;

    private String imageMapRight;

    private String imageMapCenter;




    public Map() {
    }

    public ArrayList<City> getCity() {
        return city;
    }

    public void setCity(ArrayList<City> city) {
        this.city = city;
    }

    public String getImageMapCenter() {
        return imageMapCenter;
    }

    public void setImageMapCenter(String imageMapCenter) {
        this.imageMapCenter = imageMapCenter;
    }

    public String getImageMapLeft() {
        return imageMapLeft;
    }

    public void setImageMapLeft(String imageMapLeft) {
        this.imageMapLeft = imageMapLeft;
    }

    public String getImageMapRight() {
        return imageMapRight;
    }

    public void setImageMapRight(String imageMapRight) {
        this.imageMapRight = imageMapRight;
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

    @Override
    public String toString() {
        return "Map{" +
                "city=" + city +
                ", links=" + links +
                ", mapName='" + mapName + '\'' +
                ", mapPreview='" + mapPreview + '\'' +
                ", imageMapLeft='" + imageMapLeft + '\'' +
                ", imageMapRight='" + imageMapRight + '\'' +
                ", imageMapCenter='" + imageMapCenter + '\'' +
                '}';
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
        map.setImageMapCenter("image left");
        map.setImageMapRight("Image right");
        map.setImageMapLeft("image left");
        map.setMapName("prima mappa");
        map.setMapPreview("map preview");

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
        //write();
        read();
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

}