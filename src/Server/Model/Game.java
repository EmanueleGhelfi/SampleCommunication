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

/** Game is the class that represents the single match.
 * More games are managed by GameManager. The logical part is instead handled by Gamecontroller.
 * Created by Emanuele on 11/05/2016.
 */
public class Game implements Serializable {

    private boolean started; //True if game is full (There game is started and the players are playing)
    private HashMap<String, User> usersInGame = new HashMap<>(); //All users in the game with their name
    private Map map; //All cities in undirectedgraph
    private HashMap<RegionName, Region> regions = new HashMap<>();
    private King king; //King with his cities
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

    /** Constructor and game initialization
     */
    public Game() {
        this.started = false;
        gameController = new GameController(this);
        gameController.startTimer();//create Region
        bank = new Bank();
        createRegion();// create permit card decks
        createDecks();// create nobility, victory and moneyPath
        createPaths();//create regionBonusCard, kingBonusCards, colorBonusCard
        createBonusDeck();//create Politic Card Deck
        createPoliticCards();

    }

    /** Creates the regions of the game
     */
    private void createRegion() {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        for (RegionName regionName : RegionName.values()) {
            Region region = new Region(regionName, 5, bank);
            regions.put(region.getRegion(), region);
        }
    }

    /** Creates politic cards
     *
     */
    private void createPoliticCards() {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        // foreach color create thirteen cards
        politicCards = new PoliticDeck();
        politicCards.createRandomDeck();
    }

    /** Creates region bonus, color bonus and king bonus
     */
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

    /** Creates the paths (nobility, victory and money)
     */
    private void createPaths() {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        nobilityPath = new NobilityPath();
        moneyPath = new MoneyPath();
        victoryPath = new VictoryPath();
    }

    /** A user is added to the game
     * @param userToAdd is the user to add
     * @return true if user is added
     */
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

    /** Creates permit card decks
     */
    private void createDecks() {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        permitDecks = new HashMap<>();
        for (RegionName region : RegionName.values()) {
            PermitDeck permitDeck = new PermitDeck(regions.get(region).getRegion());
            permitDeck.createRandomDeck();
            permitDecks.put(region, permitDeck);
        }
    }

    /** Pop the king bonus card deck
     */
    public void popKingBonusCard() {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        try {
            kingBonusCards.pop();
        } catch (EmptyStackException e) {
        }
    }

    /** Getter of the region
     * @param region the region i want
     * @return the region if exist
     */
    public Region getRegion(RegionName region) {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        if (regions.containsKey(region))
            return regions.get(region);
        return null;
    }

    /** Getter of the city
     * @param city is the city that i want
     * @return the city if exist
     */
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

    /** Remove an object from the market list
     * @param buyableWrapper is the object must be removed
     */
    public synchronized void removeFromMarketList(BuyableWrapper buyableWrapper) {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        marketList.remove(buyableWrapper);
    }

    /** Get king bonus card if there are
     * @return null if there are not, king bonus card else
     */
    public KingBonusCard getKingBonusCard() {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        try {
            return kingBonusCards.peek();
        } catch (EmptyStackException e) {
            return null;
        }
    }

    /** Getter
     * @return the users in game
     */
    public Collection<User> getUsers() {
        return usersInGame.values();
    }

    /** Getter
     * @return the money path
     */
    public MoneyPath getMoneyPath() {
        return moneyPath;
    }

    /** Getter
     * @return the victory path
     */
    public VictoryPath getVictoryPath() {
        return victoryPath;
    }

    /** Getter
     * @return the graph of the map
     */
    public UndirectedGraph<City, DefaultEdge> getGraph() {
        return map.getMapGraph();
    }

    /** Getter
     * @param region is the region from where i want the bonus
     * @return the bonus
     */
    public RegionBonusCard getRegionBonusCard(RegionName region) {
        return regionBonusCard.get(region);
    }

    /** Getter
     * @param color is the color of the bonus
     * @return the bonus
     */
    public ColorBonusCard getColorBonusCard(String color) {
        return colorBonusCard.get(color);
    }

    /** Getter
     * @return the King
     */
    public King getKing() {
        return king;
    }

    /** Setter
     * @param king is the king
     */
    public void setKing(King king) {
        this.king = king;
    }

    /** Getter
     * @return the nobility path
     */
    public NobilityPath getNobilityPath() {
        return nobilityPath;
    }

    /** Getter
     * @param region is the region where i want the permit card
     * @return the permit deck
     */
    public PermitDeck getPermitDeck(RegionName region) {
        return permitDecks.get(region);
    }

    /** Getter
     * @return the regions
     */
    public HashMap<RegionName, Region> getRegions() {
        return regions;
    }

    /** Getter
     * @return the colorBonusCards
     */
    public HashMap<String, ColorBonusCard> getColorBonusCard() {
        return colorBonusCard;
    }

    /** Getter
     * @return the regionBonusCard
     */
    public HashMap<RegionName, RegionBonusCard> getRegionBonusCard() {
        return regionBonusCard;
    }

    /** Getter
     * @return if is started the match
     */
    public boolean isStarted() {
        return started;
    }

    /** Setter
     * @param started is the boolean that confirm that match is started
     */
    public void setStarted(boolean started) {
        this.started = started;
    }

    /** Getter
     * @return the users in game
     */
    public HashMap<String, User> getUsersInGame() {
        return usersInGame;
    }

    /** Getter
     * @return the king bonus cards
     */
    public Stack<KingBonusCard> getKingBonusCards() {
        return kingBonusCards;
    }

    /** Getter
     * @return the gameController
     */
    public GameController getGameController() {
        return gameController;
    }

    /** Getter
     * @return the politic deck
     */
    public PoliticDeck getPoliticCards() {
        return politicCards;
    }

    /** Setter
     * @param politicCards are the politic cards that must be set
     */
    public void setPoliticCards(PoliticDeck politicCards) {
        this.politicCards = politicCards;
    }

    /** Getter
     * @return the map
     */
    public Map getMap() {
        return map;
    }

    /** Setter
     * @param map the map to set
     */
    public void setMap(Map map) {
        this.map = map;
    }

    /** Add to buyable object a new object
     * @param buyableWrapper is the object to add
     * @throws AlreadyPresentException is the exception raised
     */
    public void addBuyableWrapper(BuyableWrapper buyableWrapper) throws AlreadyPresentException {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        if (marketList.contains(buyableWrapper))
            throw new AlreadyPresentException();
        else {
            marketList.add(buyableWrapper);
        }
    }

    /** Getter
     * @param username is the username
     * @return the user with this username
     */
    public User getUser(String username) {
        return usersInGame.get(username);
    }

    /** Getter
     * @return the clone of the marketList
     */
    public ArrayList<BuyableWrapper> getMarketList() {
        return (ArrayList<BuyableWrapper>) marketList.clone();
    }

    /** Getter
     * @return the bank
     */
    public Bank getBank() {
        return bank;
    }

    /** Getter
     * @return the permit decks
     */
    public HashMap<RegionName, PermitDeck> getPermitDecks() {
        return permitDecks;
    }

    /** toString
     * @return the toString of the class
     */
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

}
