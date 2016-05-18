package Server.Model;

import CommonModel.GameModel.Card.SingleCard.PermitCard.PermitCard;
import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticCard;
import CommonModel.GameModel.City.City;
import CommonModel.GameModel.Path.Position;
import CommonModel.Snapshot.CurrentUser;
import Server.NetworkInterface.Communication.BaseCommunication;
import Server.Controller.GamesManager;

import java.util.ArrayList;

/**
 * Created by Emanuele on 11/05/2016.
 */
public class User extends CurrentUser {

    private BaseCommunication baseCommunication;

    private GamesManager gamesManager;

    private Game game;

    public User(BaseCommunication baseCommunication, GamesManager gamesManager) {
        this.baseCommunication = baseCommunication;
        this.username = "DummyId";
        this.gamesManager = gamesManager;
        this.game = null;
        this.connected=true;
        usersEmporium = new ArrayList<>();
        permitCards = new ArrayList<>();
        oldPermitCards = new ArrayList<>();
        politicCards = new ArrayList<>();
        politicCardNumber = politicCards.size();
    }


    /**
     *
     * @return the BaseCommunication (RMICommunication or SocketCommunication)
     */
    public BaseCommunication getBaseCommunication() {
        return baseCommunication;
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public void notifyGameStart() {
        baseCommunication.notifyGameStart();
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

    public void addEmporium(City cityEmporium) {
        this.usersEmporium.add(cityEmporium);
    }

    public void setHelpers(int helpers) {
        this.helpers = helpers;
    }

    public void setFastActionCounter(int fastActionCounter) {
        this.fastActionCounter = fastActionCounter;
    }

    public void setMainActionCounter(int mainActionCounter) {
        this.mainActionCounter = mainActionCounter;
    }

    public void setPoliticCards(ArrayList<PoliticCard> politicCards) {
        this.politicCards = politicCards;
        politicCardNumber = politicCards.size();
    }

    @Override
    public String toString() {
        return "User{" +
                "baseCommunication=" + baseCommunication +
                ", username='" + username + '\'' +
                ", gamesManager=" + gamesManager +
                ", game=" + game +
                ", connected=" + connected +
                ", coinPathPosition=" + coinPathPosition +
                ", victoryPathPosition=" + victoryPathPosition +
                ", nobilityPathPosition=" + nobilityPathPosition +
                ", usersEmporium=" + usersEmporium +
                ", helpers=" + helpers +
                '}';
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

    public int getPoliticCardSize(){
        return politicCards.size();
    }

    public void decrementPoliticCardNumber() {
        politicCardNumber--;
    }
}
