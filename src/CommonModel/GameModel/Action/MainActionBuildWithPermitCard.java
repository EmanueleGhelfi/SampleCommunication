package CommonModel.GameModel.Action;

import CommonModel.Snapshot.SnapshotToSend;
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
        if(super.checkActionCounter(user)) {
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
                    System.out.println("user's helper changed "+ user.getHelpers().size());
                    user.addEmporium(gameCity);
                    System.out.println("user's emporium "+user.getUsersEmporium());
                    gameCity.getBonus().getBonus(user, game);
                    // get bonus to old city near the city in wich the user wants to build
                    CityVisitor cityVisitor = new CityVisitor(game.getGraph(), user.getUsersEmporium());
                    for (City cityToVisit : cityVisitor.visit(gameCity)) {
                        if(cityToVisit!=null && cityToVisit.getBonus()!=null) {
                            cityToVisit.getBonus().getBonus(user, game);
                        }
                        else{
                            System.out.println(" "+cityToVisit+" has null bonus or is null");
                        }
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
}
