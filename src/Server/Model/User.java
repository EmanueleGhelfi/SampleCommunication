package Server.Model;

import CommonModel.GameModel.Card.SingleCard.PermitCard.PermitCard;
import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticCard;
import CommonModel.GameModel.City.City;
import CommonModel.GameModel.Council.Helper;
import CommonModel.GameModel.Market.BuyableObject;
import CommonModel.GameModel.Market.BuyableWrapper;
import CommonModel.GameModel.Path.Position;
import CommonModel.Snapshot.CurrentUser;
import Server.Controller.GameController;
import Server.NetworkInterface.Communication.BaseCommunication;
import Server.Controller.GamesManager;
import java.io.Serializable;
import java.util.*;

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
        mainActionCounter = 0;
        fastActionCounter = 0;
        optionalActionCounter=0;
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
        for(Iterator<PermitCard> itr = permitCards.iterator(); itr.hasNext();) {
            PermitCard permitCard = itr.next();
            if (permitCard.equals(permitCardToRemove)) {
                oldPermitCards.add(permitCard);
                itr.remove();
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

    // new value of helpers
    public void setHelpers(int helpers) {
        /*
        this.helpers = new ArrayList<>();
        for(int i =0; i< helpers; i++){
            this.helpers.add(new Helper() );
        }
        System.out.println(this.getHelpers().size());
        */

        if(helpers<this.helpers.size()){
            while (helpers<this.helpers.size()) {
                Helper helperToRemove = this.helpers.get(this.helpers.size() - 1);
                game.removeFromMarketList(new BuyableWrapper(helperToRemove, username));
                this.helpers.remove(helperToRemove);
            }
        }
        else{
            while (this.helpers.size()<helpers) {
                this.helpers.add(new Helper());
            }
        }
    }

    public int getPoliticCardSize(){
        return politicCards.size();
    }

    public void setFastActionCounter(int fastActionCounter) {
        System.out.println("Set fast action counter called");
        this.fastActionCounter = fastActionCounter;
        if (this.fastActionCounter == 0 && this.mainActionCounter == 0 && this.optionalActionCounter==0){
            game.getGameController().onFinishRound(this);
        }
    }

    public void setMainActionCounter(int mainActionCounter) {
        System.out.println("Set main action counter called");
        this.mainActionCounter = mainActionCounter;
        if (this.fastActionCounter == 0 && this.mainActionCounter == 0 && this.optionalActionCounter==0){
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


    public void removePoliticCard(PoliticCard buyableObject) {
        for(Iterator<PoliticCard> itr = politicCards.iterator(); itr.hasNext();){
            PoliticCard politicCard = itr.next();
            if(politicCard.equals(buyableObject)){
                itr.remove();
            }
        }
    }
    public void addHelper(){
        helpers.add(new Helper());
    }

    public void removeHelper(Helper buyableObject){
        //helpers.remove(helpers.size()-1);
        helpers.remove(buyableObject);
    }

    public PermitCard removePermitCardDefinitevely(PermitCard permitCardToRemove){
        for(Iterator<PermitCard> itr = permitCards.iterator(); itr.hasNext();){
            PermitCard permitCard = itr.next();
            if(permitCard.equals(permitCardToRemove)){
                itr.remove();
                return permitCard;
            }
        }
        return null;

    }

    public void drawCard() {
        PoliticCard politicCard = game.getPoliticCards().drawACard();
        politicCards.add(politicCard);
        politicCardNumber++;
    }

    public void addOptionalActionCounter(){
        this.optionalActionCounter++;
        if (this.fastActionCounter == 0 && this.mainActionCounter == 0 && this.optionalActionCounter==0){
            game.getGameController().onFinishRound(this);
        }

    }

    public void decrementOptionalActionCounter(){
        if(this.optionalActionCounter!=0) {
            this.optionalActionCounter--;
            if (this.fastActionCounter == 0 && this.mainActionCounter == 0 && this.optionalActionCounter == 0) {
                game.getGameController().onFinishRound(this);
            }
        }

    }


}
