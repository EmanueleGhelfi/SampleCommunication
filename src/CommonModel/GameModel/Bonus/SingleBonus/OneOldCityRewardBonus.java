package CommonModel.GameModel.Bonus.SingleBonus;

import CommonModel.GameModel.Bonus.Generic.Bonus;
import Server.Model.Game;
import Server.Model.User;
import Utilities.Exception.ActionNotPossibleException;

/**
 * Get one city bonus (also the ones used) but not the ones with nobility bonus
 * Created by Giulio on 18/05/2016.
 */
public class OneOldCityRewardBonus implements Bonus {

    //TODO
    @Override
    public void getBonus(User user, Game game) throws ActionNotPossibleException {

    }
}
