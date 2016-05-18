package CommonModel.GameModel.Bonus.SingleBonus;

import CommonModel.GameModel.Bonus.Generic.Bonus;
import Utilities.Exception.ActionNotPossibleException;
import Server.Model.Game;
import Server.Model.User;

import java.io.Serializable;

/**
 * Created by Giulio on 13/05/2016.
 */
public class PoliticCardBonus implements Bonus, Serializable {


    //TODO
    @Override
    public void getBonus(User user, Game game) throws ActionNotPossibleException {
        user.addPoliticCard(game.getPoliticCards().drawACard());
    }
}
