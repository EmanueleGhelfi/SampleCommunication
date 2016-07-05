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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Emanuele on 11/05/2016.
 */
public class User extends CurrentUser {

    private BaseCommunication baseCommunication;
    private Game game;
    private GameController gameController;

    public User() {
    }

    public User(BaseCommunication baseCommunication, GamesManager gamesManager) {
        this.baseCommunication = baseCommunication;
        username = "DummyId";
        game = null;
        connected = true;
        this.usersEmporium = new ArrayList<>();
        this.permitCards = new ArrayList<>();
        this.oldPermitCards = new ArrayList<>();
        this.politicCards = new ArrayList<>();
        this.helpers = new ArrayList<>();
        this.politicCardNumber = this.politicCards.size();
        this.mainActionCounter = 0;
        this.fastActionCounter = 0;
        this.optionalActionCounter = 0;
    }

    public void addEmporium(City cityEmporium) {
        usersEmporium.add(cityEmporium);
    }

    /**
     * Add permit card to user permit cards
     *
     * @param permitCard permit card to add
     */
    public void addPermitCard(PermitCard permitCard) {
        this.permitCards.add(permitCard);
    }

    /**
     * Removes permit card in user permit card and it to old user permit cards
     *
     * @param permitCardToRemove the permit card to remove
     */
    public void removePermitCard(PermitCard permitCardToRemove) {
        for (Iterator<PermitCard> itr = this.permitCards.iterator(); itr.hasNext(); ) {
            PermitCard permitCard = itr.next();
            if (permitCard.equals(permitCardToRemove)) {
                this.oldPermitCards.add(permitCard);
                this.game.removeFromMarketList(new BuyableWrapper(permitCard, this.username));
                itr.remove();
            }
        }
    }

    public void addPoliticCard(PoliticCard politicCard) {
        this.politicCards.add(politicCard);
        this.politicCardNumber++;
    }

    public void decrementPoliticCardNumber() {
        this.politicCardNumber--;
    }

    @Override
    public String toString() {
        return "User{" +
                "baseCommunication=" + this.baseCommunication +
                ", username='" + this.username + '\'' +
                ", game=" + this.game +
                ", connected=" + this.connected +
                ", coinPathPosition=" + this.coinPathPosition +
                ", victoryPathPosition=" + this.victoryPathPosition +
                ", nobilityPathPosition=" + this.nobilityPathPosition +
                ", usersEmporium=" + this.usersEmporium +
                ", helpers=" + this.helpers +
                '}';
    }

    // new value of helpers
    public void setHelpers(int helpers) {

        if (helpers < this.helpers.size()) {
            while (helpers < this.helpers.size()) {
                Helper helperToRemove = this.helpers.get(this.helpers.size() - 1);
                this.game.removeFromMarketList(new BuyableWrapper(helperToRemove, this.username));
                this.helpers.remove(helperToRemove);
            }
        } else {
            while (this.helpers.size() < helpers) {
                this.helpers.add(new Helper());
            }
        }
    }

    public int getPoliticCardSize() {
        return this.politicCards.size();
    }

    public void setFastActionCounter(int fastActionCounter) {
        System.out.println("Set fast action counter called");
        this.fastActionCounter = fastActionCounter;
        if (this.fastActionCounter == 0 && mainActionCounter == 0 && optionalActionCounter == 0) {
            this.game.getGameController().onFinishRound(this);
        }
    }

    public void setMainActionCounter(int mainActionCounter) {
        System.out.println("Set main action counter called");
        this.mainActionCounter = mainActionCounter;
        if (fastActionCounter == 0 && this.mainActionCounter == 0 && optionalActionCounter == 0) {
            this.game.getGameController().onFinishRound(this);
        }
    }

    public void setPoliticCards(ArrayList<PoliticCard> politicCards) {
        this.politicCards = politicCards;
        this.politicCardNumber = politicCards.size();
    }

    public BaseCommunication getBaseCommunication() {
        return this.baseCommunication;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public void setCoinPathPosition(int coinPathPosition) {
        this.coinPathPosition = coinPathPosition;
    }

    public void setVictoryPathPosition(int victoryPathPosition) {
        this.victoryPathPosition = victoryPathPosition;
    }

    @Override
    public Position getNobilityPathPosition() {
        return this.nobilityPathPosition;
    }

    public void setNobilityPathPosition(Position nobilityPathPosition) {
        this.nobilityPathPosition = nobilityPathPosition;
    }

    public Game getGame() {
        return this.game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public GameController getGameController() {
        return this.gameController;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public void removePoliticCard(PoliticCard buyableObject) {
        for (Iterator<PoliticCard> itr = this.politicCards.iterator(); itr.hasNext(); ) {
            PoliticCard politicCard = itr.next();
            if (politicCard.equals(buyableObject)) {
                itr.remove();
            }
        }
    }

    public void addHelper() {
        this.helpers.add(new Helper());
    }

    public void removeHelper(Helper buyableObject) {
        //helpers.remove(helpers.size()-1);
        this.helpers.remove(buyableObject);
    }

    public PermitCard removePermitCardDefinitevely(PermitCard permitCardToRemove) {
        for (Iterator<PermitCard> itr = this.permitCards.iterator(); itr.hasNext(); ) {
            PermitCard permitCard = itr.next();
            if (permitCard.equals(permitCardToRemove)) {
                itr.remove();
                return permitCard;
            }
        }
        return null;

    }

    public void drawCard() {
        PoliticCard politicCard = this.game.getPoliticCards().drawACard();
        this.politicCards.add(politicCard);
        this.politicCardNumber++;
    }

    public void addOptionalActionCounter() {
        optionalActionCounter++;
        if (fastActionCounter == 0 && mainActionCounter == 0 && optionalActionCounter == 0) {
            this.game.getGameController().onFinishRound(this);
        }

    }

    public void decrementOptionalActionCounter() {
        if (optionalActionCounter != 0) {
            optionalActionCounter--;
            if (fastActionCounter == 0 && mainActionCounter == 0 && optionalActionCounter == 0) {
                this.game.getGameController().onFinishRound(this);
            }
        }

    }


    public void removeHelper() {
        helpers.remove(this.helpers.size() - 1);
    }
}
