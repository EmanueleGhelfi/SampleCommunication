package Server.Model;

import CommonModel.GameModel.Bonus.Reward.ColorBonusCard;
import CommonModel.GameModel.Bonus.Reward.KingBonusCard;
import CommonModel.GameModel.Bonus.Reward.RegionBonusCard;
import CommonModel.GameModel.Card.Deck.PermitDeck;
import CommonModel.GameModel.Card.Deck.PoliticDeck;
import CommonModel.GameModel.City.City;
import CommonModel.GameModel.City.Color;
import CommonModel.GameModel.City.Region;
import CommonModel.GameModel.City.RegionName;
import CommonModel.GameModel.Council.Bank;
import CommonModel.GameModel.Council.King;
import CommonModel.GameModel.Market.BuyableWrapper;
import CommonModel.GameModel.Path.MoneyPath;
import CommonModel.GameModel.Path.NobilityPath;
import CommonModel.GameModel.Path.VictoryPath;
import Server.Controller.GameController;
import Utilities.Class.Constants;
import Utilities.Exception.AlreadyPresentException;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import java.io.Serializable;
import java.util.*;

/**
 * Created by Emanuele on 11/05/2016.
 */
public class Game implements Serializable {

    /**
     * True if game is full (There game is started and the players are playing)
     */
    private boolean started;

    /**
     * All users in the game with their name
     */
    private final HashMap<String, User> usersInGame = new HashMap<>();

    /**
     * All cities in undirectedgraph
     */
    private Map map;
    private final HashMap<RegionName, Region> regions = new HashMap<>();

    /**
     * King with his cities
     */
    private King king;

    // PATHS
    private VictoryPath victoryPath;
    private NobilityPath nobilityPath;
    private MoneyPath moneyPath;
    private final GameController gameController;
    private final Bank bank;

    // PERMIT DECK
    private HashMap<RegionName, PermitDeck> permitDecks;

    // POLITIC CARD
    private PoliticDeck politicCards;
    private final HashMap<RegionName, RegionBonusCard> regionBonusCard = new HashMap<>();
    private final HashMap<String, ColorBonusCard> colorBonusCard = new HashMap<>();
    private final Stack<KingBonusCard> kingBonusCards = new Stack<>();

    //list of buyable wrapper, all object that user can buy
    private final ArrayList<BuyableWrapper> marketList = new ArrayList<>();

    public Game() {
        started = false;
        this.gameController = new GameController(this);
        this.gameController.startTimer();
        //create Region
        this.bank = new Bank();
        this.createRegion();
        // create permit card decks
        this.createDecks();
        // create nobility, victory and moneyPath
        this.createPaths();
        // create city Graph
        this.createCityGraph();
        //create regionBonusCard, kingBonusCards, colorBonusCard
        this.createBonusDeck();
        //create Politic Card Deck
        this.createPoliticCards();

    }

    private void createRegion() {
        for (RegionName regionName : RegionName.values()) {
            Region region = new Region(regionName, 5, this.bank);
            this.regions.put(region.getRegion(), region);
        }
    }

    private void createPoliticCards() {
        // foreach color create thirteen cards
        this.politicCards = new PoliticDeck();
        this.politicCards.createRandomDeck();
    }

    private void createBonusDeck() {
        //region bonus
        for (RegionName region : RegionName.values()) {
            this.regionBonusCard.put(region, new RegionBonusCard(this.regions.get(region)));
        }
        // color bonus
        for (Color color : Color.values()) {
            this.colorBonusCard.put(color.getColor(), new ColorBonusCard(color));
        }
        //king bonus
        for (int i = 1; i < Constants.KING_CARDS; i++) {
            this.kingBonusCards.add(new KingBonusCard(Constants.KING_CARDS - i));
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
        this.nobilityPath = new NobilityPath();
        this.moneyPath = new MoneyPath();
        this.victoryPath = new VictoryPath();
    }

    public boolean addUserToGame(User userToAdd) {
        System.out.println("ADDING A USER TO A GAME " + userToAdd.getUsername());
        if (!this.usersInGame.containsKey(userToAdd.getUsername())) {
            this.usersInGame.put(userToAdd.getUsername(), userToAdd);
            if (this.usersInGame.size() >= 2 && this.usersInGame.size() < Constants.MAX_CLIENT_NUMBER) {
                this.gameController.setTimeout();
            } else if (this.usersInGame.size() == Constants.MAX_CLIENT_NUMBER) {
                this.gameController.cancelTimeout();
                this.gameController.notifyStarted();
            }
            return true;
        }
        return false;
    }

    private void createDecks() {
        //create mountainDeck
        this.permitDecks = new HashMap<>();
        for (RegionName region : RegionName.values()) {
            PermitDeck permitDeck = new PermitDeck(this.regions.get(region).getRegion());
            permitDeck.createRandomDeck();
            this.permitDecks.put(region, permitDeck);
        }
    }

    public void popKingBonusCard() {
        try {
            this.kingBonusCards.pop();
        } catch (EmptyStackException e) {
        }
    }

    @Override
    public String toString() {
        return "Game{" +
                ", started=" + this.started +
                ", regions=" + this.regions +
                ", king=" + this.king +
                ", victoryPath=" + this.victoryPath +
                ", nobilityPath=" + this.nobilityPath +
                ", moneyPath=" + this.moneyPath +
                ", gameController=" + this.gameController +
                ", permitDecks=" + this.permitDecks +
                ", politicCards=" + this.politicCards +
                ", regionBonusCard=" + this.regionBonusCard +
                ", colorBonusCard=" + this.colorBonusCard +
                ", kingBonusCards=" + this.kingBonusCards +
                '}';
    }

    public Region getRegion(RegionName region) {
        if (this.regions.containsKey(region))
            return this.regions.get(region);
        return null;
    }

    public City getCity(City city) {
        if (this.map.getMapGraph().containsVertex(city)) {
            for (City cityToSearch : this.map.getMapGraph().vertexSet()) {
                if (cityToSearch.equals(city)) {
                    return cityToSearch;
                }
            }
        }
        return null;
    }

    /**
     * get king bonus card if there are
     *
     * @return null if there are not, king bonus cardelse
     */
    public KingBonusCard getKingBonusCard() {
        try {
            return this.kingBonusCards.peek();
        } catch (EmptyStackException e) {
            return null;
        }
    }

    public Collection<User> getUsers() {
        return this.usersInGame.values();
    }

    public MoneyPath getMoneyPath() {
        return this.moneyPath;
    }

    public VictoryPath getVictoryPath() {
        return this.victoryPath;
    }

    public UndirectedGraph<City, DefaultEdge> getGraph() {
        return this.map.getMapGraph();
    }

    public RegionBonusCard getRegionBonusCard(RegionName region) {
        return this.regionBonusCard.get(region);
    }

    public ColorBonusCard getColorBonusCard(String color) {
        return this.colorBonusCard.get(color);
    }

    public King getKing() {
        return this.king;
    }

    public void setKing(King king) {
        this.king = king;
    }

    public NobilityPath getNobilityPath() {
        return this.nobilityPath;
    }

    public PermitDeck getPermitDeck(RegionName region) {
        return this.permitDecks.get(region);
    }

    public HashMap<RegionName, Region> getRegions() {
        return this.regions;
    }

    public HashMap<String, ColorBonusCard> getColorBonusCard() {
        return this.colorBonusCard;
    }

    public HashMap<RegionName, RegionBonusCard> getRegionBonusCard() {
        return this.regionBonusCard;
    }

    public boolean isStarted() {
        return this.started;
    }

    // inizializzazione partita
    public void setStarted(boolean started) {
        this.started = started;
    }

    public HashMap<String, User> getUsersInGame() {
        return this.usersInGame;
    }

    public Stack<KingBonusCard> getKingBonusCards() {
        return this.kingBonusCards;
    }

    public GameController getGameController() {
        return this.gameController;
    }

    public PoliticDeck getPoliticCards() {
        return this.politicCards;
    }

    public void setPoliticCards(PoliticDeck politicCards) {
        this.politicCards = politicCards;
    }

    public Map getMap() {
        return this.map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public void addBuyableWrapper(BuyableWrapper buyableWrapper) throws AlreadyPresentException {
        if (this.marketList.contains(buyableWrapper))
            throw new AlreadyPresentException();
        else {
            this.marketList.add(buyableWrapper);
        }
    }


    public User getUser(String username) {
        return this.usersInGame.get(username);
    }

    public ArrayList<BuyableWrapper> getMarketList() {
        return (ArrayList<BuyableWrapper>) this.marketList.clone();
    }

    public synchronized void removeFromMarketList(BuyableWrapper buyableWrapper) {
        System.out.println("Before remove : " + this.marketList.size());
        this.marketList.remove(buyableWrapper);
        System.out.println("After remove :" + this.marketList.size());
    }

    public Bank getBank() {
        return this.bank;
    }

    public HashMap<RegionName, PermitDeck> getPermitDecks() {
        return this.permitDecks;
    }
}
