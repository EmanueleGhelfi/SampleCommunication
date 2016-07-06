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
import Utilities.Class.InternalLog;
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
    private HashMap<String, User> usersInGame = new HashMap<>();

    /**
     * All cities in undirectedgraph
     */
    private Map map;
    private HashMap<RegionName, Region> regions = new HashMap<>();

    /**
     * King with his cities
     */
    private King king;

    // PATHS
    private VictoryPath victoryPath;
    private NobilityPath nobilityPath;
    private MoneyPath moneyPath;
    private GameController gameController;
    private Bank bank;

    // PERMIT DECK
    private HashMap<RegionName, PermitDeck> permitDecks;

    // POLITIC CARD
    private PoliticDeck politicCards;
    private HashMap<RegionName, RegionBonusCard> regionBonusCard = new HashMap<>();
    private HashMap<String, ColorBonusCard> colorBonusCard = new HashMap<>();
    private Stack<KingBonusCard> kingBonusCards = new Stack<>();

    //list of buyable wrapper, all object that user can buy
    private ArrayList<BuyableWrapper> marketList = new ArrayList<>();

    public Game() {
        this.started = false;
        gameController = new GameController(this);
        gameController.startTimer();
        //create Region
        bank = new Bank();
        createRegion();
        // create permit card decks
        createDecks();
        // create nobility, victory and moneyPath
        createPaths();
        //create regionBonusCard, kingBonusCards, colorBonusCard
        createBonusDeck();
        //create Politic Card Deck
        createPoliticCards();

    }

    private void createRegion() {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        for (RegionName regionName : RegionName.values()) {
            Region region = new Region(regionName, 5, bank);
            regions.put(region.getRegion(), region);
        }
    }

    private void createPoliticCards() {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        // foreach color create thirteen cards
        politicCards = new PoliticDeck();
        politicCards.createRandomDeck();
    }

    private void createBonusDeck() {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        //region bonus
        for (RegionName region : RegionName.values()) {
            regionBonusCard.put(region, new RegionBonusCard(regions.get(region)));
        }
        // color bonus
        for (Color color : Color.values()) {
            colorBonusCard.put(color.getColor(), new ColorBonusCard(color));
        }
        //king bonus
        for (int i = 1; i < Constants.KING_CARDS; i++) {
            kingBonusCards.add(new KingBonusCard(Constants.KING_CARDS - i));
        }
    }

    private void createPaths() {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        nobilityPath = new NobilityPath();
        moneyPath = new MoneyPath();
        victoryPath = new VictoryPath();
    }

    public boolean addUserToGame(User userToAdd) {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        if (!usersInGame.containsKey(userToAdd.getUsername())) {
            usersInGame.put(userToAdd.getUsername(), userToAdd);
            if (usersInGame.size() >= 2 && usersInGame.size() < Constants.MAX_CLIENT_NUMBER) {
                gameController.setTimeout();
            } else if (usersInGame.size() == Constants.MAX_CLIENT_NUMBER) {
                gameController.cancelTimeout();
                gameController.notifyStarted();
            }
            return true;
        }
        return false;
    }

    private void createDecks() {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        //create mountainDeck
        permitDecks = new HashMap<>();
        for (RegionName region : RegionName.values()) {
            PermitDeck permitDeck = new PermitDeck(regions.get(region).getRegion());
            permitDeck.createRandomDeck();
            permitDecks.put(region, permitDeck);
        }
    }

    public void popKingBonusCard() {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        try {
            kingBonusCards.pop();
        } catch (EmptyStackException e) {
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

    public Region getRegion(RegionName region) {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        if (regions.containsKey(region))
            return regions.get(region);
        return null;
    }

    public City getCity(City city) {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        if (map.getMapGraph().containsVertex(city)) {
            for (City cityToSearch : map.getMapGraph().vertexSet()) {
                if (cityToSearch.equals(city)) {
                    return cityToSearch;
                }
            }
        }
        return null;
    }

    public synchronized void removeFromMarketList(BuyableWrapper buyableWrapper) {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        System.out.println("Before remove : " + marketList.size());
        marketList.remove(buyableWrapper);
        System.out.println("After remove :" + marketList.size());
    }

    /**
     * get king bonus card if there are
     *
     * @return null if there are not, king bonus cardelse
     */
    public KingBonusCard getKingBonusCard() {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        try {
            return kingBonusCards.peek();
        } catch (EmptyStackException e) {
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

    public RegionBonusCard getRegionBonusCard(RegionName region) {
        return regionBonusCard.get(region);
    }

    public ColorBonusCard getColorBonusCard(String color) {
        return colorBonusCard.get(color);
    }

    public King getKing() {
        return king;
    }

    public void setKing(King king) {
        this.king = king;
    }

    public NobilityPath getNobilityPath() {
        return nobilityPath;
    }

    public PermitDeck getPermitDeck(RegionName region) {
        return permitDecks.get(region);
    }

    public HashMap<RegionName, Region> getRegions() {
        return regions;
    }

    public HashMap<String, ColorBonusCard> getColorBonusCard() {
        return colorBonusCard;
    }

    public HashMap<RegionName, RegionBonusCard> getRegionBonusCard() {
        return regionBonusCard;
    }

    public boolean isStarted() {
        return started;
    }

    // inizializzazione partita
    public void setStarted(boolean started) {
        this.started = started;
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

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public void addBuyableWrapper(BuyableWrapper buyableWrapper) throws AlreadyPresentException {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        if (marketList.contains(buyableWrapper))
            throw new AlreadyPresentException();
        else {
            marketList.add(buyableWrapper);
        }
    }

    public User getUser(String username) {
        return usersInGame.get(username);
    }

    public ArrayList<BuyableWrapper> getMarketList() {
        return (ArrayList<BuyableWrapper>) marketList.clone();
    }

    public Bank getBank() {
        return bank;
    }

    public HashMap<RegionName, PermitDeck> getPermitDecks() {
        return permitDecks;
    }
}
