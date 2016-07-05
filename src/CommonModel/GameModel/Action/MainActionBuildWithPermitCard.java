package CommonModel.GameModel.Action;

import Utilities.Class.Constants;
import Utilities.Exception.ActionNotPossibleException;
import CommonModel.GameModel.Card.SingleCard.PermitCard.PermitCard;
import CommonModel.GameModel.City.*;
import Server.Model.Game;
import Server.Model.User;

/**
 * Created by Giulio on 16/05/2016.
 */
public class MainActionBuildWithPermitCard extends Action{

    private PermitCard permitCard;
    private City city;

    public MainActionBuildWithPermitCard(City city, PermitCard permitCard) {
        this.city = city;
        this.permitCard = permitCard;
        this.actionType = Constants.MAIN_ACTION;
    }

    @Override
    public void doAction(Game game, User user) throws ActionNotPossibleException {
        if(super.checkActionCounter(user) && permitCard!=null && city!=null && user.getPermitCards().contains(permitCard)) {
            boolean actionPossible = false;
            for (char cityInitial : permitCard.getCityAcronimous()) {
                if (city.getCityName().getCityName().charAt(0) == cityInitial)
                    actionPossible = true;
            }
            if (actionPossible) {
                City gameCity = game.getCity(city);
                int helperToSpend = 0;
                // find helpers to spend (if there are emporiums of other players)
                if (checkEmporiumsAreNotTen(user) && checkEmporiumsIsAlreadyPresent(user, gameCity)) {
                    for (User userToFind : game.getUsers()) {
                        if (userToFind.getUsersEmporium().contains(gameCity)) {
                            helperToSpend++;
                        }
                    }
                    // if user can build
                    if (user.getHelpers().size() >= helperToSpend) {
                        user.setHelpers(user.getHelpers().size() - helperToSpend);
                        user.addEmporium(gameCity);
                        if (gameCity.getBonus() != null) {
                            gameCity.getBonus().getBonus(user, game);
                        }
                        // get bonus to old city near the city in which the user wants to build
                        super.getNearCityBonus(game,user,gameCity);
                        //check region and color bonus
                        checkRegionBonus(gameCity, user, game);
                        checkColorBonus(gameCity, user, game);
                        // add to old permit card
                        user.removePermitCard(permitCard);
                        removeAction(game, user);
                        if (user.getUsersEmporium().size() == 10) {
                            game.getGameController().startingLastRound();
                        }
                    } else {
                        throw new ActionNotPossibleException(Constants.HELPER_EXCEPTION);
                    }
                }
            }
            else{
                throw new ActionNotPossibleException(Constants.CITY_NOT_CORRECT_EXCEPTION);
            }
        }
        else {
            throw new ActionNotPossibleException("City or Permit card not selected");
        }
    }

    @Override
    public String toString() {
        return "[MAIN ACTION] Build with permit card "+ permitCard.getCityAcronimous()+" in city: "+city.getCityName()+".";
    }
}
