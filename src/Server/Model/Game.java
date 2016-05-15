package Server.Model;

import CommonModel.GameImmutable;
import CommonModel.GameModel.*;
import Server.Controller.GameController;
import Server.UserClasses.User;
import com.sun.corba.se.impl.orbutil.graph.Graph;
import com.sun.corba.se.impl.orbutil.graph.GraphImpl;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.util.*;

/**
 * Created by Emanuele on 11/05/2016.
 */
public class Game implements GameImmutable{

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
    private PermitDeck mountainDeck;

    private PermitDeck hillDeck;

    private PermitDeck coastDeck;

    // POLITIC CARD
    private ArrayList<PoliticCard> politicCards;
    
    
    public Game() {
        this.started = false;
        gameController = new GameController(this);
        gameController.startTimer();
        cities = new SimpleGraph<City, DefaultEdge>(DefaultEdge.class);
        createDecks();
        createPaths();
        createCityGraph();
    }

    private void createCityGraph() {
        //TODO: create graph and connection between city
    }

    private void createPaths() {
        nobilityPath = new NobilityPath();
        moneyPath = new MoneyPath();
        victoryPath = new VictoryPath();
    }


    @Override
    public boolean isStarted() {
        return started;
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
        mountainDeck = new PermitDeck(Region.MOUNTAIN);
        hillDeck = new PermitDeck(Region.HILL);
        coastDeck = new PermitDeck(Region.COAST);
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
}
