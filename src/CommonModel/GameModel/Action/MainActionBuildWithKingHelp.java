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

    private ArrayList<City> kingPath;
    private ArrayList<PoliticCard> politicCards;
    private int bonusCounter = 0;

    public MainActionBuildWithKingHelp(ArrayList<City> kingPath, ArrayList<PoliticCard> politicCards) {
        this.kingPath = kingPath;
        this.politicCards = politicCards;
        this.actionType=Constants.MAIN_ACTION;
    }

    @Override
    public void doAction(Game game, User user) throws ActionNotPossibleException {
        // count number of correct politic card
        int correctPoliticCard = 0;
        // region of permit card
        King king = game.getKing();
        //this is the new position of the user in money path
        int newPositionInMoneyPath = 0;

        int helperToSpend=0;
        //true if the emporiums are not ten and i haven't build in that city
        if(super.checkActionCounter(user) && kingPath!=null && politicCards!=null && pathIsCorrect(game) && politicCards.size()>0) {
            if (checkEmporiumsAreNotTen(user) && kingPath.size() > 0 &&
                    checkEmporiumsIsAlreadyPresent(user, kingPath.get(kingPath.size() - 1))) {
                // city where king goes
                City kingCity = game.getCity(kingPath.get(kingPath.size() - 1));
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
                    correctPoliticCard = countCorrectPoliticCard(king, politicCards, bonusCounter);
                    // calculate money to spend
                    newPositionInMoneyPath = calculateMoney(correctPoliticCard, politicCards, bonusCounter);
                    if ((kingPath.size() - 1) * Constants.KING_PRICE + newPositionInMoneyPath <= user.getCoinPathPosition()) {
                        for (City city : kingPath) {
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
                        super.getNearCityBonus(game, user, kingCity);
                    } else {
                        throw new ActionNotPossibleException(Constants.MONEY_EXCEPTION);
                    }
                    // go ahead in money path for politic card
                    game.getMoneyPath().goAhead(user, -newPositionInMoneyPath);
                    // re-add to game deck
                    game.getPoliticCards().addToQueue(new HashSet<>(politicCards));
                    // remove cards from user
                    removePoliticCard(politicCards, user, game);
                    //check region and color bonus
                    checkRegionBonus(kingCity, user, game);
                    checkColorBonus(kingCity, user, game);
                    moveKing(game, user);
                    removeAction(game, user);
                    if (user.getUsersEmporium().size() == 10) {
                        game.getGameController().startingLastRound();
                    }
                } else {
                    throw new ActionNotPossibleException(Constants.HELPER_EXCEPTION);
                }
            } else {
                throw new ActionNotPossibleException(Constants.EMPORIUM_PRESENT_EXCEPTION);
            }
        }
            else {
                if (politicCards == null || politicCards.size() == 0) {
                    throw new ActionNotPossibleException(Constants.POLITIC_CARD_EXCEPTION);
                }
                throw new ActionNotPossibleException(Constants.INCORRECT_PATH_EXCEPTION);
            }

    }

    private boolean pathIsCorrect(Game game) {
        UndirectedGraph<City,DefaultEdge> mapGraph = game.getMap().getMapGraph();
        if(kingPath.size()>0 && !kingPath.get(0).equals(game.getKing().getCurrentCity())){
            return false;
        }
        for(int i = 0; i<kingPath.size()-1;i++){
            NeighborIndex<City,DefaultEdge> neighborIndex = new NeighborIndex(mapGraph);
            if(!(neighborIndex.neighborListOf(kingPath.get(i)).contains(kingPath.get(i+1)))){
                return false;
            }
        }
        return true;
    }

    private void moveKing(Game game, User user) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        game.getUsers().forEach(user1 -> {

            Runnable runnable =()-> {
                user1.getBaseCommunication().moveKing(kingPath);
            };

            executorService.execute(runnable);
        });
    }

    @Override
    public String toString() {
        if(kingPath.size()==0){
            return "";
        }
        return "[MAIN ACTION] Build an empory in city " + kingPath.get(kingPath.size()-1).getCityName()+" with king help.\n";
    }
}
