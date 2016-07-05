package CommonModel.GameModel.Action;

import CommonModel.GameModel.Card.SingleCard.PermitCard.PermitCard;
import CommonModel.GameModel.City.City;
import Server.Model.Game;
import Server.Model.User;
import Utilities.Class.Constants;
import Utilities.Exception.ActionNotPossibleException;

/**
 * Created by Giulio on 16/05/2016.
 */
public class MainActionBuildWithPermitCard extends Action {

    private final PermitCard permitCard;
    private final City city;

    public MainActionBuildWithPermitCard(City city, PermitCard permitCard) {
        this.city = city;
        this.permitCard = permitCard;
        actionType = Constants.MAIN_ACTION;
    }

    @Override
    public void doAction(Game game, User user) throws ActionNotPossibleException {
        if (checkActionCounter(user) && this.permitCard != null && this.city != null && user.getPermitCards().contains(this.permitCard)) {
            boolean actionPossible = false;
            for (char cityInitial : this.permitCard.getCityAcronimous()) {
                if (this.city.getCityName().getCityName().charAt(0) == cityInitial)
                    actionPossible = true;
            }
            if (actionPossible) {
                City gameCity = game.getCity(this.city);
                int helperToSpend = 0;
                // find helpers to spend (if there are emporiums of other players)
                if (this.checkEmporiumsAreNotTen(user) && this.checkEmporiumsIsAlreadyPresent(user, gameCity)) {
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
                        getNearCityBonus(game, user, gameCity);
                        //check region and color bonus
                        this.checkRegionBonus(gameCity, user, game);
                        this.checkColorBonus(gameCity, user, game);
                        // add to old permit card
                        user.removePermitCard(this.permitCard);
                        this.removeAction(game, user);
                        if (user.getUsersEmporium().size() == 10) {
                            game.getGameController().startingLastRound();
                        }
                    } else {
                        throw new ActionNotPossibleException(Constants.HELPER_EXCEPTION);
                    }
                }
            } else {
                throw new ActionNotPossibleException(Constants.CITY_NOT_CORRECT_EXCEPTION);
            }
        } else {
            throw new ActionNotPossibleException("City or Permit card not selected");
        }
    }

    @Override
    public String toString() {
        return "[MAIN ACTION] Build with permit card " + this.permitCard.getCityAcronimous() + " in city: " + this.city.getCityName() + ".";
    }
}
