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
        this.type = Constants.MAIN_ACTION;
    }

    @Override
    public void doAction(Game game, User user) throws ActionNotPossibleException {
        City gameCity = game.getCity(city);
        int helperToSpend = 0;
        // find helpers to spend (if there are emporiums of other players
        if (checkEmporiumsAreNotTen(user) && checkEmporiumsIsAlreadyPresent(user, gameCity)) {
            for (User userToFind : game.getUsers()) {
                if (userToFind.getUsersEmporium().contains(gameCity)) {
                    helperToSpend++;
                }
            }
            // if user can build
            if (user.getHelpers() >= helperToSpend) {
                user.setHelpers(user.getHelpers() - helperToSpend);
                user.addEmporium(gameCity);
                gameCity.getBonus().getBonus(user, game);
                // get bonus to old city near the city in wich the user wants to build
                CityVisitor cityVisitor = new CityVisitor(game.getGraph(), user.getUsersEmporium());
                for (City cityToVisit : cityVisitor.visit(gameCity)) {
                    cityToVisit.getBonus().getBonus(user, game);
                }
                checkRegionBonus(gameCity, user, game);
                checkColorBonus(gameCity, user, game);
                // add to old permit card
                user.removePermitCard(permitCard);
                removeAction(game, user);
            } else {
                throw new ActionNotPossibleException();
            }
        }
    }
}
