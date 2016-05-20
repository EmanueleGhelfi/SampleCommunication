package Server.Model;

import CommonModel.GameModel.Card.SingleCard.PermitCard.PermitCard;
import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticCard;
import CommonModel.GameModel.City.City;
import CommonModel.GameModel.Path.Position;
import CommonModel.Snapshot.CurrentUser;
import Server.Controller.GameController;
import Server.NetworkInterface.Communication.BaseCommunication;
import Server.Controller.GamesManager;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Emanuele on 11/05/2016.
 */
public class User extends CurrentUser implements Serializable{

    private BaseCommunication baseCommunication;
    private Game game;
    private GameController gameController;

    public User() {
    }

    public User(BaseCommunication baseCommunication, GamesManager gamesManager) {
        this.baseCommunication = baseCommunication;
        this.username = "DummyId";
        this.game = null;
        this.connected=true;
        usersEmporium = new ArrayList<>();
        permitCards = new ArrayList<>();
        oldPermitCards = new ArrayList<>();
        politicCards = new ArrayList<>();
        politicCardNumber = politicCards.size();
    }


    public void addEmporium(City cityEmporium) {
        this.usersEmporium.add(cityEmporium);
    }

    /**
     * Add permit card to user permit cards
     * @param permitCard permit card to add
     */
    public void addPermitCard(PermitCard permitCard){
        permitCards.add(permitCard);
    }

    /**
     * Removes permit card in user permit card and it to old user permit cards
     * @param permitCardToRemove the permit card to remove
     */
    public void removePermitCard(PermitCard permitCardToRemove){
        for (PermitCard permitCard: permitCards) {
            if (permitCard.equals(permitCardToRemove)){
                permitCards.remove(permitCard);
                oldPermitCards.add(permitCardToRemove);
            }

        }
    }

    public void addPoliticCard(PoliticCard politicCard){
        politicCards.add(politicCard);
        politicCardNumber++;
    }

    public void decrementPoliticCardNumber() {
        politicCardNumber--;
    }

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

    public void setHelpers(int helpers) {
        this.helpers = helpers;
    }
    public int getPoliticCardSize(){
        return politicCards.size();
    }
    public void setFastActionCounter(int fastActionCounter) {
        this.fastActionCounter = fastActionCounter;
        if (this.fastActionCounter == 0 && this.mainActionCounter == 0){
            game.getGameController().onFinishRound(this);
        }
    }
    public void setMainActionCounter(int mainActionCounter) {
        this.mainActionCounter = mainActionCounter;
        if (this.fastActionCounter == 0 && this.mainActionCounter == 0){
            game.getGameController().onFinishRound(this);
        }
    }
    public void setPoliticCards(ArrayList<PoliticCard> politicCards) {
        this.politicCards = politicCards;
        politicCardNumber = politicCards.size();
    }
    public BaseCommunication getBaseCommunication() {
        return baseCommunication;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setConnected(boolean connected) {
        this.connected = connected;
    }
    public void setGame(Game game) {
        this.game = game;
    }
    public void setCoinPathPosition(int coinPathPosition) {
        this.coinPathPosition = coinPathPosition;
    }
    public void setVictoryPathPosition(int victoryPathPosition) {
        this.victoryPathPosition = victoryPathPosition;
    }
    public Position getNobilityPathPosition() {
        return nobilityPathPosition;
    }
    public void setNobilityPathPosition(Position nobilityPathPosition) {
        this.nobilityPathPosition = nobilityPathPosition;
    }
    public Game getGame() {
        return game;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public GameController getGameController() {
        return gameController;
    }
}
