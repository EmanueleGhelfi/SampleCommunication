package Server.Model;

import CommonModel.GameModel.Bonus.Reward.ColorBonusCard;
import CommonModel.GameModel.Bonus.Reward.KingBonusCard;
import CommonModel.GameModel.Bonus.Reward.RegionBonusCard;
import CommonModel.GameModel.Card.Deck.PermitDeck;
import CommonModel.GameModel.Card.Deck.PoliticDeck;
import CommonModel.GameModel.City.City;
import CommonModel.GameModel.City.Color;
import CommonModel.GameModel.City.Region;
import CommonModel.GameModel.Council.King;
import CommonModel.GameModel.Path.MoneyPath;
import CommonModel.GameModel.Path.NobilityPath;
import CommonModel.GameModel.Path.VictoryPath;
import Server.Controller.GameController;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.util.*;

/**
 * Created by Emanuele on 11/05/2016.
 */
public class Game{

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
    private UndirectedGraph<City,DefaultEdge> cities;
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
    public PoliticDeck getPoliticCards() {
        return politicCards;
    }
    public void setPoliticCards(PoliticDeck politicCards) {
        this.politicCards = politicCards;
    }

    // POLITIC CARD
    private PoliticDeck politicCards;
    private UndirectedGraph<City, DefaultEdge> graph;
    private HashMap<String,RegionBonusCard> regionBonusCard = new HashMap<>();
    private HashMap<String,ColorBonusCard> colorBonusCard = new HashMap<>();
    private Stack<KingBonusCard> kingBonusCards = new Stack<>();

    public Game() {
        this.started = false;
        gameController = new GameController(this);
        gameController.startTimer();
        cities = new SimpleGraph<City, DefaultEdge>(DefaultEdge.class);
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
        king = new King();
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
        //TODO: Add multicolor card
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
        for (int i= 1; i<6;i++){
            kingBonusCards.add(new KingBonusCard(i));
        }
    }

    private void createCityGraph() {
        //TODO: create graph and connection between city
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
            if(usersInGame.size()>=2 && usersInGame.size()<4){
                gameController.setTimeout();
            }
            else if(usersInGame.size()>=2){
                gameController.cancelTimeout();
                gameController.notifyStarted();
            }
            return true;
        }
        return false;
    }

    public void setStarted(boolean started) {
        // inizializzazione partita
        this.started = started;
    }

    private void createDecks() {
        //create mountainDeck
        permitDecks = new HashMap<Region, PermitDeck>();
        for (Region region : Region.values()) {
            permitDecks.put(region, new PermitDeck(region));
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

    public Region getRegion(String region){
        if(regions.containsKey(region))
            return regions.get(region);
        return null;
    }

    public City getCity(City city) {
        if(cities.containsVertex(city)){
            for (City cityToSearch: cities.vertexSet()) {
                if (cityToSearch.equals(city)){
                    return cityToSearch;
                }
            }
        }
        return null;
    }

    public void setGraph(UndirectedGraph<City, DefaultEdge> graph) {
        this.graph = graph;
    }

    public UndirectedGraph<City, DefaultEdge> getGraph() {
        return graph;
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

    public void setNobilityPath(NobilityPath nobilityPath) {
        this.nobilityPath = nobilityPath;
    }

    public KingBonusCard getKingBonusCard() {
        try{
            return kingBonusCards.peek();
        } catch (EmptyStackException e){
            return null;
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
                "cities=" + cities +
                ", started=" + started +
                ", regions=" + regions +
                ", king=" + king +
                ", victoryPath=" + victoryPath +
                ", nobilityPath=" + nobilityPath +
                ", moneyPath=" + moneyPath +
                ", gameController=" + gameController +
                ", permitDecks=" + permitDecks +
                ", politicCards=" + politicCards +
                ", graph=" + graph +
                ", regionBonusCard=" + regionBonusCard +
                ", colorBonusCard=" + colorBonusCard +
                ", kingBonusCards=" + kingBonusCards +
                '}';
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
}
