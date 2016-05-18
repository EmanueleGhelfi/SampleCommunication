package CommonModel.GameModel.Bonus.SingleBonus;

import CommonModel.GameModel.Bonus.Generic.Bonus;
import Utilities.Exception.ActionNotPossibleException;
import Server.Model.Game;
import Server.Model.User;

import java.io.Serializable;

/**
 * Get two city bonus (also the ones used) but not the ones with nobility bonus
 * Created by Giulio on 13/05/2016.
 */
public class OldPermitCardBonus implements Bonus,Serializable {

    //TODO
    @Override
    public void getBonus(User user, Game game) throws ActionNotPossibleException {

    }
}
