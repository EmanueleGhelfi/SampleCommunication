package CommonModel.GameModel.Action;

import Utilities.Class.Constants;
import Utilities.Exception.ActionNotPossibleException;
import CommonModel.GameModel.Bonus.Reward.KingBonusCard;
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
        // find helpers to spend
        for (User userToFind: game.getUsers()) {
            if (userToFind.getUsersEmporium().contains(gameCity)){
                helperToSpend++;
            }
        }
        // if user can build
        if (user.getHelpers()>=helperToSpend){
            user.setHelpers(user.getHelpers()-helperToSpend);
            user.addEmporium(gameCity);
            gameCity.getBonus().getBonus(user, game);
            // get bonus to old city near the city in wich the user wants to build
            CityVisitor cityVisitor = new CityVisitor(game.getGraph(), user.getUsersEmporium());
            for (City cityToVisit : cityVisitor.visit(gameCity)) {
                cityToVisit.getBonus().getBonus(user, game);
            }
            // check region bonus
            if (gameCity.getRegion().checkRegion(user.getUsersEmporium())){
                game.getRegionBonusCard(gameCity.getRegion().getRegion()).getBonus(user, game);
                // check king bonus and get it
                KingBonusCard kingBonusCard = game.getKingBonusCard();
                if (kingBonusCard != null){
                    kingBonusCard.getBonus(user, game);
                }
            }
            // check color bonus
            if (gameCity.getColor().checkColor(user.getUsersEmporium())){
                game.getColorBonusCard(gameCity.getColor().getColor()).getBonus(user, game);
                // check king bonus and get it
                KingBonusCard kingBonusCard = game.getKingBonusCard();
                if (kingBonusCard != null){
                    kingBonusCard.getBonus(user, game);
                }
            }
            // add to old permit card
            user.removePermitCard(permitCard);
            removeAction(game,user);
        } else {
            throw new ActionNotPossibleException();
        }
    }
}
