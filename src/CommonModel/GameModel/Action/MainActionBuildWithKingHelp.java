package CommonModel.GameModel.Action;

import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticCard;
import CommonModel.GameModel.City.City;
import CommonModel.GameModel.Council.King;
import Server.Model.Game;
import Server.Model.User;
import Utilities.Class.Constants;
import Utilities.Exception.ActionNotPossibleException;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.alg.NeighborIndex;
import org.jgrapht.graph.DefaultEdge;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Giulio on 18/05/2016.
 */
public class MainActionBuildWithKingHelp extends Action {

    private final ArrayList<City> kingPath;
    private final ArrayList<PoliticCard> politicCards;
    private int bonusCounter;

    public MainActionBuildWithKingHelp(ArrayList<City> kingPath, ArrayList<PoliticCard> politicCards) {
        this.kingPath = kingPath;
        this.politicCards = politicCards;
        actionType = Constants.MAIN_ACTION;
    }

    @Override
    public void doAction(Game game, User user) throws ActionNotPossibleException {
        // count number of correct politic card
        int correctPoliticCard = 0;
        // region of permit card
        King king = game.getKing();
        //this is the new position of the user in money path
        int newPositionInMoneyPath = 0;

        int helperToSpend = 0;
        //true if the emporiums are not ten and i haven't build in that city
        if (checkActionCounter(user) && this.kingPath != null && this.politicCards != null && this.pathIsCorrect(game) && this.politicCards.size() > 0) {
            if (this.checkEmporiumsAreNotTen(user) && this.kingPath.size() > 0 &&
                    this.checkEmporiumsIsAlreadyPresent(user, this.kingPath.get(this.kingPath.size() - 1))) {
                // city where king goes
                City kingCity = game.getCity(this.kingPath.get(this.kingPath.size() - 1));
                for (User userToFind : game.getUsers()) {
                    if (userToFind.getUsersEmporium().contains(kingCity)) {
                        helperToSpend++;
                    }
                }
                // if user can build
                if (user.getHelpers().size() >= helperToSpend) {
                    //decrement helper
                    user.setHelpers(user.getHelpers().size() - helperToSpend);
                    // calculate correct politic card
                    correctPoliticCard = this.countCorrectPoliticCard(king, this.politicCards, this.bonusCounter);
                    // calculate money to spend
                    newPositionInMoneyPath = this.calculateMoney(correctPoliticCard, this.politicCards, this.bonusCounter);
                    if ((this.kingPath.size() - 1) * Constants.KING_PRICE + newPositionInMoneyPath <= user.getCoinPathPosition()) {
                        for (City city : this.kingPath) {
                            //user.setCoinPathPosition(user.getCoinPathPosition() - Constants.KING_PRICE);
                            game.getMoneyPath().goAhead(user, -Constants.KING_PRICE);
                            //king.setCurrentCity(city);
                        }
                        king.setCurrentCity(kingCity);
                        // because of first element
                        game.getMoneyPath().goAhead(user, Constants.KING_PRICE);
                        user.addEmporium(kingCity);
                        if (!kingCity.getColor().getColor().equals(Constants.PURPLE)) {
                            kingCity.getBonus().getBonus(user, game);
                        }
                        //check near bonus
                        getNearCityBonus(game, user, kingCity);
                    } else {
                        throw new ActionNotPossibleException(Constants.MONEY_EXCEPTION);
                    }
                    // go ahead in money path for politic card
                    game.getMoneyPath().goAhead(user, -newPositionInMoneyPath);
                    // re-add to game deck
                    game.getPoliticCards().addToQueue(new HashSet<>(this.politicCards));
                    // remove cards from user
                    this.removePoliticCard(this.politicCards, user, game);
                    //check region and color bonus
                    this.checkRegionBonus(kingCity, user, game);
                    this.checkColorBonus(kingCity, user, game);
                    this.moveKing(game, user);
                    this.removeAction(game, user);
                    if (user.getUsersEmporium().size() == 10) {
                        game.getGameController().startingLastRound();
                    }
                } else {
                    throw new ActionNotPossibleException(Constants.HELPER_EXCEPTION);
                }
            } else {
                throw new ActionNotPossibleException(Constants.EMPORIUM_PRESENT_EXCEPTION);
            }
        } else {
            if (this.politicCards == null || this.politicCards.size() == 0) {
                throw new ActionNotPossibleException(Constants.POLITIC_CARD_EXCEPTION);
            }
            throw new ActionNotPossibleException(Constants.INCORRECT_PATH_EXCEPTION);
        }

    }

    private boolean pathIsCorrect(Game game) {
        UndirectedGraph<City, DefaultEdge> mapGraph = game.getMap().getMapGraph();
        if (this.kingPath.size() > 0 && !this.kingPath.get(0).equals(game.getKing().getCurrentCity())) {
            return false;
        }
        for (int i = 0; i < this.kingPath.size() - 1; i++) {
            NeighborIndex<City, DefaultEdge> neighborIndex = new NeighborIndex(mapGraph);
            if (!neighborIndex.neighborListOf(this.kingPath.get(i)).contains(this.kingPath.get(i + 1))) {
                return false;
            }
        }
        return true;
    }

    private void moveKing(Game game, User user) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        game.getUsers().forEach(user1 -> {

            Runnable runnable = () -> {
                user1.getBaseCommunication().moveKing(this.kingPath);
            };

            executorService.execute(runnable);
        });
    }

    @Override
    public String toString() {
        if (this.kingPath.size() == 0) {
            return "";
        }
        return "[MAIN ACTION] Build an empory in city " + this.kingPath.get(this.kingPath.size() - 1).getCityName() + " with king help.\n";
    }
}
