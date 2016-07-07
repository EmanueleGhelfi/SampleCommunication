package Server.Model;

import CommonModel.GameModel.Card.SingleCard.PermitCard.PermitCard;
import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticCard;
import CommonModel.GameModel.City.City;
import CommonModel.GameModel.Council.Helper;
import CommonModel.GameModel.Market.BuyableWrapper;
import CommonModel.GameModel.Path.Position;
import CommonModel.Snapshot.CurrentUser;
import Server.Controller.GameController;
import Server.Controller.GamesManager;
import Server.NetworkInterface.Communication.BaseCommunication;
import Utilities.Class.InternalLog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/** This class is intended to represent the user in the server.
 * In this class we will find all the information visible server side and only changed here.
 * Otherwise, users will not be sent a copy of this in when it contains the Setter.
 * Created by Emanuele on 11/05/2016.
 */
public class User extends CurrentUser implements Serializable {

    private BaseCommunication baseCommunication;
    private Game game;
    private GameController gameController;

    /** Constructor used for serialization
     */
    public User() {
    }

    /** Constructor
     * @param baseCommunication is the type of communication
     * @param gamesManager is the games manager
     */
    public User(BaseCommunication baseCommunication, GamesManager gamesManager) {
        this.baseCommunication = baseCommunication;
        this.username = "DummyId";
        this.game = null;
        this.connected = true;
        usersEmporium = new ArrayList<>();
        permitCards = new ArrayList<>();
        oldPermitCards = new ArrayList<>();
        politicCards = new ArrayList<>();
        helpers = new ArrayList<>();
        politicCardNumber = politicCards.size();
        mainActionCounter = 0;
        fastActionCounter = 0;
        optionalActionCounter = 0;
    }

    /** Add emporia to user
     * @param cityEmporium is the city where this user has build
     */
    public void addEmporium(City cityEmporium) {
        this.usersEmporium.add(cityEmporium);
    }

    /** Add permit card to user permit cards
     * @param permitCard permit card to add
     */
    public void addPermitCard(PermitCard permitCard) {
        permitCards.add(permitCard);
    }

    /** Removes permit card in user permit card and it to old user permit cards
     * @param permitCardToRemove the permit card to remove
     */
    public void removePermitCard(PermitCard permitCardToRemove) {
        for (Iterator<PermitCard> itr = permitCards.iterator(); itr.hasNext(); ) {
            PermitCard permitCard = itr.next();
            if (permitCard.equals(permitCardToRemove)) {
                oldPermitCards.add(permitCard);
                game.removeFromMarketList(new BuyableWrapper(permitCard, username));
                itr.remove();
            }
        }
    }

    /** Add the politic card to the user hand
     * @param politicCard is the politic card to add
     */
    public void addPoliticCard(PoliticCard politicCard) {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        politicCards.add(politicCard);
        politicCardNumber++;
    }

    /** Decrement the politic card number
     */
    public void decrementPoliticCardNumber() {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        politicCardNumber--;
    }

    /** New value of helpers
     * @param helpers are the helpers
     */
    public void setHelpers(int helpers) {
        if (helpers < this.helpers.size()) {
            while (helpers < this.helpers.size()) {
                Helper helperToRemove = this.helpers.get(this.helpers.size() - 1);
                game.removeFromMarketList(new BuyableWrapper(helperToRemove, username));
                this.helpers.remove(helperToRemove);
            }
        } else {
            while (this.helpers.size() < helpers) {
                this.helpers.add(new Helper());
            }
        }
    }

    /** Getter
     * @return the politic card size
     */
    public int getPoliticCardSize() {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        return politicCards.size();
    }

    /** Setter
     * @param fastActionCounter is the counter of the fast action
     */
    public void setFastActionCounter(int fastActionCounter) {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        this.fastActionCounter = fastActionCounter;
        if (this.fastActionCounter == 0 && this.mainActionCounter == 0 && this.optionalActionCounter == 0) {
            game.getGameController().onFinishRound(this);
        }
    }

    /** Setter
     * @param mainActionCounter is the counter of the main action
     */
    public void setMainActionCounter(int mainActionCounter) {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        this.mainActionCounter = mainActionCounter;
        if (this.fastActionCounter == 0 && this.mainActionCounter == 0 && this.optionalActionCounter == 0) {
            game.getGameController().onFinishRound(this);
        }
    }

    /** Setter
     * @param politicCards are the politic cards that must be set
     */
    public void setPoliticCards(ArrayList<PoliticCard> politicCards) {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        this.politicCards = politicCards;
        politicCardNumber = politicCards.size();
    }

    /** Getter
     * @return is the type of the communication
     */
    public BaseCommunication getBaseCommunication() {
        return baseCommunication;
    }

    /** Setter
     * @param username the name of the player
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /** Setter
     * @param connected is the boolean that specify if he is connected
     */
    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    /** Setter
     * @param coinPathPosition is the coin path position to set
     */
    public void setCoinPathPosition(int coinPathPosition) {
        this.coinPathPosition = coinPathPosition;
    }

    /** Setter
     * @param victoryPathPosition is the victory path position to set
     */
    public void setVictoryPathPosition(int victoryPathPosition) {
        this.victoryPathPosition = victoryPathPosition;
    }

    /** Getter
     * @return the position in nobility path
     */
    @Override
    public Position getNobilityPathPosition() {
        return nobilityPathPosition;
    }

    /** Setter
     * @param nobilityPathPosition is the nobility path position to set
     */
    public void setNobilityPathPosition(Position nobilityPathPosition) {
        this.nobilityPathPosition = nobilityPathPosition;
    }

    /** Getter
     * @return the game
     */
    public Game getGame() {
        return game;
    }

    /** Setter
     * @param game is the game to set
     */
    public void setGame(Game game) {
        this.game = game;
    }

    /** Getter
     * @return the game controller
     */
    public GameController getGameController() {
        return gameController;
    }

    /** Setter
     * @param gameController is the game controller must be set
     */
    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    /** Remove the politic card from user hand
     * @param buyableObject is the object that must be removed
     */
    public void removePoliticCard(PoliticCard buyableObject) {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        for (Iterator<PoliticCard> itr = politicCards.iterator(); itr.hasNext(); ) {
            PoliticCard politicCard = itr.next();
            if (politicCard.equals(buyableObject)) {
                itr.remove();
            }
        }
    }

    /** Add an helper
     */
    public void addHelper() {
        helpers.add(new Helper());
    }

    /** Remove an helper
     * @param buyableObject is the helper that must be removed
     */
    public void removeHelper(Helper buyableObject) {
        helpers.remove(buyableObject);
    }

    /** Remove the permit card
     * @param permitCardToRemove is the permit card to remove
     * @return the permit card removed
     */
    public PermitCard removePermitCardDefinitevely(PermitCard permitCardToRemove) {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        for (Iterator<PermitCard> itr = permitCards.iterator(); itr.hasNext(); ) {
            PermitCard permitCard = itr.next();
            if (permitCard.equals(permitCardToRemove)) {
                itr.remove();
                return permitCard;
            }
        }
        return null;

    }

    /** Draw a politic card
     */
    public void drawCard() {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        PoliticCard politicCard = game.getPoliticCards().drawACard();
        politicCards.add(politicCard);
        politicCardNumber++;
    }

    /** Add optional action counter
     */
    public void addOptionalActionCounter() {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        this.optionalActionCounter++;
        if (this.fastActionCounter == 0 && this.mainActionCounter == 0 && this.optionalActionCounter == 0) {
            game.getGameController().onFinishRound(this);
        }

    }

    /** Remove the optional action counter
     */
    public void decrementOptionalActionCounter() {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        if (this.optionalActionCounter != 0) {
            this.optionalActionCounter--;
            if (this.fastActionCounter == 0 && this.mainActionCounter == 0 && this.optionalActionCounter == 0) {
                game.getGameController().onFinishRound(this);
            }
        }

    }

    /** toString
     * @return the toString of the class
     */
    @Override
    public String toString() {
        return "User{" +
                "baseCommunication=" + baseCommunication +
                ", username='" + username + '\'' +
                ", game=" + game +
                ", connected=" + connected +
                ", coinPathPosition=" + coinPathPosition +
                ", victoryPathPosition=" + victoryPathPosition +
                ", nobilityPathPosition=" + nobilityPathPosition +
                ", usersEmporium=" + usersEmporium +
                ", helpers=" + helpers +
                '}';
    }

}
