package CommonModel.GameModel.Action;

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
        this.type = "MAIN_ACTION";
    }

    @Override
    public void doAction(Game game, User user) throws ActionNotPossibleException {
        City gameCity = game.getCity(city);
        int helperToSpend = 0;
        for (User userToFind: game.getUsers()) {
            if (userToFind.getUsersEmporium().contains(gameCity)){
                helperToSpend++;
            }
        }
        if (user.getHelpers()>=helperToSpend){
            user.setHelpers(user.getHelpers()-helperToSpend);
            user.addEmporium(gameCity);
            gameCity.getBonus().getBonus(user, game);
            CityVisitor cityVisitor = new CityVisitor(game.getGraph(), user.getUsersEmporium());
            for (City cityToVisit : cityVisitor.visit(gameCity)) {
                cityToVisit.getBonus().getBonus(user, game);
            }
            if (gameCity.getRegion().checkRegion(user.getUsersEmporium())){
                game.getRegionBonusCard(gameCity.getRegion().getRegion()).getBonus(user, game);
                KingBonusCard kingBonusCard = game.getKingBonusCard();
                if (kingBonusCard != null){
                    kingBonusCard.getBonus(user, game);
                }
            }
            if (gameCity.getColor().checkColor(user.getUsersEmporium())){
                game.getColorBonusCard(gameCity.getColor().getColor()).getBonus(user, game);
                KingBonusCard kingBonusCard = game.getKingBonusCard();
                if (kingBonusCard != null){
                    kingBonusCard.getBonus(user, game);
                }
            }
            removeAction(game,user);
        } else {
            throw new ActionNotPossibleException();
        }

//
    }


}
