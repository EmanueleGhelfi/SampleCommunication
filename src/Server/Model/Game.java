package Server.Model;

import CommonModel.GameModel.Bonus.Reward.ColorBonusCard;
import CommonModel.GameModel.Bonus.Reward.KingBonusCard;
import CommonModel.GameModel.Bonus.Reward.RegionBonusCard;
import CommonModel.GameModel.Card.Deck.PermitDeck;
import CommonModel.GameModel.Card.Deck.PoliticDeck;
import CommonModel.GameModel.Card.SingleCard.PermitCard.PermitCard;
import CommonModel.GameModel.City.*;
import CommonModel.GameModel.Council.King;
import CommonModel.GameModel.Path.MoneyPath;
import CommonModel.GameModel.Path.NobilityPath;
import CommonModel.GameModel.Path.VictoryPath;
import Server.Controller.GameController;
import Utilities.Class.Constants;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.io.Serializable;
import java.util.*;

/**
 * Created by Emanuele on 11/05/2016.
 */
public class Game implements Serializable{

    /**
     * True if game is full (There game is started and the players are playing)
     */
    private boolean started;

    /**
     * All users in the game with their name
     */
    private HashMap<String,User> usersInGame = new HashMap<>();

    /**
     * All cities in undirectedgraph
     */
    private Map map;
    private HashMap<String,Region> regions = new HashMap<>();

    /**
     * King with his cities
     */
    private King king;

    // PATHS
    private VictoryPath victoryPath;
    private NobilityPath nobilityPath;
    private MoneyPath moneyPath;
    private GameController gameController;

    // PERMIT DECK
    private HashMap<Region,PermitDeck> permitDecks;

    // POLITIC CARD
    private PoliticDeck politicCards;
    private HashMap<String,RegionBonusCard> regionBonusCard = new HashMap<>();
    private HashMap<String,ColorBonusCard> colorBonusCard = new HashMap<>();
    private Stack<KingBonusCard> kingBonusCards = new Stack<>();

    public Game() {
        this.started = false;
        gameController = new GameController(this);
        gameController.startTimer();
        // create permit card decks
        createDecks();
        // create nobility, victory and moneyPath
        createPaths();
        // create city Graph
        createCityGraph();
        //create regionBonusCard, kingBonusCards, colorBonusCard
        createBonusDeck();
        //create Politic Card Deck
        createPoliticCards();
        //create Region
        createRegion();
        //create king
        //king = new King();
    }

    private void createRegion() {
        for (Region region:Region.values()) {
            regions.put(region.getRegion(),region);
            System.out.println(region);
        }
    }

    private void createPoliticCards() {
        // foreach color create thirteen cards
        politicCards = new PoliticDeck();
        politicCards.createRandomDeck();
    }

    private void createBonusDeck() {
        //region bonus
        for (Region region:Region.values()) {
            regionBonusCard.put(region.getRegion(),new RegionBonusCard(region));
        }
        // color bonus
        for (Color color:Color.values()) {
            colorBonusCard.put(color.getColor(),new ColorBonusCard(color));
        }
        //king bonus
        for (int i = 1; i< Constants.KING_CARDS; i++){
            kingBonusCards.add(new KingBonusCard(i));
        }
    }

    // to remove
    private void createCityGraph() {
        /*
        City city1 = new City(Color.BLUE, CityName.ARKON,Region.COAST);
        City city2 = new City(Color.GREY,CityName.BURGEN,Region.COAST);
        City city3 = new City(Color.BLUE,CityName.KULTOS,Region.COAST);
        City city4 = new City(Color.GREY,CityName.NARIS,Region.COAST);
        City city5 = new City(Color.BLUE,CityName.OSIUM,Region.COAST);
        City city6 = new City(Color.GREY,CityName.GRADEN,Region.COAST);
        graph = new SimpleGraph<>(DefaultEdge.class);
        graph.addVertex(city1);
        graph.addVertex(city2);
        graph.addVertex(city3);
        graph.addVertex(city4);
        graph.addVertex(city5);
        graph.addVertex(city6);
        graph.addEdge(city1,city2);
        graph.addEdge(city2,city5);
        graph.addEdge(city2,city4);
        graph.addEdge(city3,city4);
        graph.addEdge(city4,city5);
        for (City cityToVisit : graph.vertexSet()) {
            System.out.println(cityToVisit);
        }
        */
    }

    private void createPaths() {
        nobilityPath = new NobilityPath();
        moneyPath = new MoneyPath();
        victoryPath = new VictoryPath();
    }

    public boolean addUserToGame(User userToAdd) {
        System.out.println("ADDING A USER TO A GAME "+userToAdd);
        if(!usersInGame.containsKey(userToAdd.getUsername())){
            usersInGame.put(userToAdd.getUsername(),userToAdd);
            if(usersInGame.size()>=2 && usersInGame.size()<Constants.MAX_CLIENT_NUMBER){
                gameController.setTimeout();
            }
            else if(usersInGame.size()==Constants.MAX_CLIENT_NUMBER){
                gameController.cancelTimeout();
                gameController.notifyStarted();
            }
            return true;
        }
        return false;
    }

    private void createDecks() {
        //create mountainDeck
        permitDecks = new HashMap<>();
        for (Region region : Region.values()) {
            PermitDeck permitDeck = new PermitDeck(region);
            permitDeck.createRandomDeck();
            permitDecks.put(region, permitDeck);
        }
    }

    public void popKingBonusCard(){
        try {
            kingBonusCards.pop();
        } catch (EmptyStackException e){
        }
    }

    @Override
    public String toString() {
        return "Game{" +
                ", started=" + started +
                ", regions=" + regions +
                ", king=" + king +
                ", victoryPath=" + victoryPath +
                ", nobilityPath=" + nobilityPath +
                ", moneyPath=" + moneyPath +
                ", gameController=" + gameController +
                ", permitDecks=" + permitDecks +
                ", politicCards=" + politicCards +
                ", regionBonusCard=" + regionBonusCard +
                ", colorBonusCard=" + colorBonusCard +
                ", kingBonusCards=" + kingBonusCards +
                '}';
    }
    // inizializzazione partita
    public void setStarted(boolean started) {
        this.started = started;
    }
    public Region getRegion(String region){
        if(regions.containsKey(region))
            return regions.get(region);
        return null;
    }

    public City getCity(City city) {
        if(map.getMapGraph().containsVertex(city)){
            for (City cityToSearch: map.getMapGraph().vertexSet()) {
                if (cityToSearch.equals(city)){
                    return cityToSearch;
                }
            }
        }
        return null;
    }

    /** get king bonus card if there are
     * @return null if there are not, king bonus cardelse
     */
    public KingBonusCard getKingBonusCard() {
        try{
            return kingBonusCards.pop();
        } catch (EmptyStackException e){
            return null;
        }
    }
    public Collection<User> getUsers() {
        return usersInGame.values();
    }
    public MoneyPath getMoneyPath() {
        return moneyPath;
    }
    public VictoryPath getVictoryPath() {
        return victoryPath;
    }
    public UndirectedGraph<City, DefaultEdge> getGraph() {
        return map.getMapGraph();
    }
    public RegionBonusCard getRegionBonusCard(String region) {
        return regionBonusCard.get(region);
    }
    public ColorBonusCard getColorBonusCard(String color) {
        return colorBonusCard.get(color);
    }
    public King getKing() {
        return king;
    }
    public NobilityPath getNobilityPath() {
        return nobilityPath;
    }
    public PermitDeck getPermitDeck(Region region){
        return permitDecks.get(region);
    }
    public HashMap<String, Region> getRegions() {
        return regions;
    }
    public HashMap<String, ColorBonusCard> getColorBonusCard() {
        return colorBonusCard;
    }
    public HashMap<String, RegionBonusCard> getRegionBonusCard() {
        return regionBonusCard;
    }
    public boolean isStarted() {
        return started;
    }
    public HashMap<String, User> getUsersInGame() {
        return usersInGame;
    }
    public Stack<KingBonusCard> getKingBonusCards() {
        return kingBonusCards;
    }
    public GameController getGameController() {
        return gameController;
    }
    public PoliticDeck getPoliticCards() {
        return politicCards;
    }
    public void setPoliticCards(PoliticDeck politicCards) {
        this.politicCards = politicCards;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public Map getMap() {
        return map;
    }

    public void setKing(King king) {
        this.king = king;
    }
}
